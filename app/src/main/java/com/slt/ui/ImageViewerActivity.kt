package com.slt.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.slt.R
import com.slt.base.BaseActivity
import com.slt.extra.Constants
import kotlinx.android.synthetic.main.activity_image_viewer.*

class ImageViewerActivity : BaseActivity(R.layout.activity_image_viewer) {

    lateinit var imageList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }

        imageList = Constants.historyMOdel

        val adapter = ImageViewPageAdapter(applicationContext, imageList)
        viewPager.adapter =adapter

        val dots = arrayOfNulls<ImageView>(imageList.size)
        if (dots.size != 1) {
            layoutDots.visibility = View.VISIBLE
            for (i in imageList.indices) {
                dots[i] = ImageView(this)
                dots[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.non_active_dot
                    )
                )
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 0, 8, 0)
                layoutDots.addView(dots.get(i), params)
            }
        } else {
            layoutDots.visibility = View.GONE
        }


        dots[0]?.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.active_dot
            )
        )

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                for (i in 0 until imageList.size) {
                    dots[i]!!.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.non_active_dot
                        )
                    )
                }

                dots[position]!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.active_dot
                    )
                )

            }

        })

    }

    class ImageViewPageAdapter(
        applicationContext: Context,
        resources: List<String>
    ) : PagerAdapter() {
        var context: Context = applicationContext
        var resources: List<String> = resources
        var onShare: ((fileName: String) -> Unit)? = null
        lateinit var fileName : String
        override fun getCount(): Int {
            return resources.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as RelativeLayout
        }


        @SuppressLint("SetTextI18n")
        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val itemView =
                LayoutInflater.from(context).inflate(R.layout.layout_image_view, null, false)
            val iv_student_feed_image_fullview_id: PhotoView =
                itemView.findViewById(R.id.iv_student_feed_image_fullview_id) as PhotoView
//
            Glide.with(context)
                .load(resources[position])
                .into(iv_student_feed_image_fullview_id)

            container.addView(itemView)
            return itemView
        }

        override fun destroyItem(
            container: ViewGroup,
            position: Int,
            `object`: Any
        ) {
            container.removeView(`object` as RelativeLayout)
        }
    }

}