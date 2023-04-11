package com.slt.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.slt.extra.Constants
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.slt.R
import timber.log.Timber
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow


@SuppressLint("StaticFieldLeak")
var mainSnackView: View? = null
var mSnack: Snackbar? = null

@SuppressLint("StaticFieldLeak")
var mToast: Toast? = null
var onSnackBarCallback: ((isShow: Boolean) -> Unit)? = null


fun isValidEmail(target: CharSequence?): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun Context.makeException(responseCode: Int, message: String?, view: View) {
    when (responseCode) {
        Constants.SESSION_EXPIRE -> {
            makeSnake(message, view)
            /* GlobalScope.launch {
                 DataManager.getInstance().getDatabase().clearAllTables()
                 DataManager.getInstance().getPreference().clearPreference()
                 startActivity(
                     Intent(
                         applicationContext,
                         SplashActivity::class.java
                     ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                 )
             }*/
        }
        Constants.TRY_AGAIN,
        Constants.NOT_FOUND -> {
            makeSnake(message, view)
        }
        Constants.INVALID_REQUEST -> {
            makeSnake(this.getString(R.string.invalid_request), view)
        }
        Constants.NO_INTERNET -> {
            makeSnake(this.getString(R.string.offline), view, true)
        }
        else -> {
            makeSnake(this.getString(R.string.something_went_wrong), view)
        }
    }
}

fun Context.makeExceptionFalse(responseCode: Boolean, message: String?, view: View) {
    if (!responseCode) {
        makeSnake(this.getString(R.string.something_went_wrong), view)
    }
}

fun Context.makeSnake(message: String?, view: View, isOffline: Boolean = false) {
    mainSnackView = view
    message?.let {
        if (mSnack != null) {
            mSnack?.dismiss()
            onSnackBarCallback?.invoke(false)
        }
        mSnack = Snackbar.make(
            view,
            it,
            if (isOffline) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG
        )
        mSnack?.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar?>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                onSnackBarCallback?.invoke(false)
            }

            override fun onShown(transientBottomBar: Snackbar?) {
                super.onShown(transientBottomBar)
                onSnackBarCallback?.invoke(true)
            }
        })
        val sbView: View? = mSnack?.view
        sbView?.setBackgroundResource(R.drawable.bg_snackbar)
        mSnack?.show()
    }
}

fun Context.dismissSnackBar(isConnected: Boolean) {
    if (isConnected) {
        mSnack?.setText("Online")
        mSnack?.dismiss()
        onSnackBarCallback?.invoke(false)
    } else {
        mainSnackView?.let {
            this.makeSnake(this.getString(R.string.offline), it, true)
        }
    }
}

fun Context.makeToast(message: String?) {
    message?.let {
        if (mToast != null)
            mToast?.cancel()
        mToast = Toast.makeText(this, it, Toast.LENGTH_SHORT)
        mToast?.show()
    }
}


fun getDatetime(milliSeconds: Long, dateFormat: String): String? {
    // Create a DateFormatter object for displaying date in specified format.
    @SuppressLint("SimpleDateFormat") val formatter =
        SimpleDateFormat(dateFormat)

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return formatter.format(calendar.time)
}

fun getFileSize(size: Long): String? {
    if (size <= 0) return "0"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups =
        (log10(size.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(
        size / 1024.0.pow(digitGroups.toDouble())
    ).toString() + " " + units[digitGroups]
}

fun Context.hideKeyboard(view: View) {
    val imm: InputMethodManager =
        this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.openUrl(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    this.startActivity(browserIntent)
}
fun Context.localBroadcastManager() = LocalBroadcastManager.getInstance(this)
fun Context.notificationManager() = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

fun showLog(message  :String)
{
    Timber.i(message)
}