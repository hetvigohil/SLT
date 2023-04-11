package com.slt.base

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.slt.R
import kotlinx.android.synthetic.main.lay_progress.*

abstract class BaseActivity(@LayoutRes val contentLayoutId: Int) :
    AppCompatActivity(contentLayoutId) {

    //    private lateinit var checkConnectivity: CheckConnectivity
    private lateinit var mBaseContext: AppCompatActivity
    private var mToast: Toast? = null
    var isBack: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaseContext = this
//        checkConnectivity = CheckConnectivity()
//        checkConnectivity.connectivityReceiverListener = this
    }


    fun setLightStatusBar(color: Int = R.color.colorThemeWhite) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        val acWindow = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            acWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        acWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            acWindow.statusBarColor = ContextCompat.getColor(applicationContext, color)
        }
    }


    fun openActivity(intent: Intent) {
        startActivity(intent)
    }


    /**
     * This Method for set full screen activity
     */
    fun setFullScreenActivity(isStable: Boolean = false) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
            decorView.systemUiVisibility =
                if (isStable) View.SYSTEM_UI_FLAG_LAYOUT_STABLE else View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarColor = Color.TRANSPARENT
            }
        }
    }

    fun makeToast(message: String?) {
        message?.let {
            if (mToast != null)
                mToast?.cancel()

            mToast = Toast.makeText(mBaseContext, it, Toast.LENGTH_SHORT)
            mToast?.show()
        }
    }

    fun showLoading() {
        panelProgress?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        panelProgress?.visibility = View.GONE
    }

    fun setStatusColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, color)
        }
    }

}