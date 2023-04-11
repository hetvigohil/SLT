package com.slt.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.slt.R
import com.slt.base.BaseActivity
import com.slt.data.datamanager.DataManager
import com.slt.data.preferences.PreferenceManager

class SplashActivity : BaseActivity(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({

            if (DataManager.getInstance().getPreference().getString(PreferenceManager.ISLOGIN, "")
                    .isEmpty().not()
            ) {
                startActivity(Intent(this, DashBoardActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
        }, 1000)

    }
}