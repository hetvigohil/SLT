package com.slt.extra

class Utils {

    fun mobileNumberFormat(text: String): String {
        val text: String = text.toString()
        val textLength: Int = text.length
        var numberText = ""
        if (text.endsWith("-") || text.endsWith(" ") || text.endsWith(" ")) text
        if (textLength == 4) {
            if (!text.contains("-")) {
                numberText = StringBuilder(text).insert(text.length - 1, "-").toString()
            }
        } else if (textLength == 8) {
            numberText = StringBuilder(text).insert(text.length - 1, "-").toString()
        }
        return numberText
    }

    companion object {
        fun insertPeriodically(text: String, insert: String, period: Int): String {
            val builder: StringBuilder = StringBuilder(
                text.length + insert.length * (text.length / period) + 1
            )

            var index = 0
            var prefix = ""

            while (index < text.length) {
                // Don't put the insert in the very first iteration.
                // This is easier than appending it *after* each substring
                builder.append(prefix)
                prefix = insert
                builder.append(
                    text.substring(
                        index,
                        Math.min(index + period, text.length)
                    )
                )
                index += period
            }
            return builder.toString()

        }

        /* fun showDialog(context: Context) {
             val dialog = Dialog(context)
             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
 //            dialog.setCancelable(false)
             dialog.setContentView(R.layout.lay_point_as_gift_dialog)
             dialog.show()

             dialog.getWindow()?.setGravity(Gravity.BOTTOM);


         }*/

        /*fun Toast.showCustomToast(message: String, activity: Activity, type:Int)
        {
            val layout = activity.layoutInflater.inflate (
                R.layout.custom_toast_layout,
                activity.findViewById(R.id.toast_container)
            )

            layout.tvMessage.text = message
            if (type == 1){
                layout.ivImage.setBackgroundResource(R.drawable.ic_server_down)
            }else{
                layout.ivImage.setBackgroundResource(R.drawable.ic_no_internet)
            }

            // use the application extension function
            this.apply {
                setGravity(Gravity.BOTTOM, 0, 40)
                duration = Toast.LENGTH_LONG
                view = layout
                show()
            }
        }*/

    }

}