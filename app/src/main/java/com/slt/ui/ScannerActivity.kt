package com.slt.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.Result
import com.slt.R
import com.slt.base.BaseActivity
import com.slt.extensions.makeException
import com.slt.extra.Constants
import com.slt.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_scanner.*
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler

class ScannerActivity : BaseActivity(R.layout.activity_scanner), ResultHandler {

    private lateinit var mHomeViewModel: HomeViewModel
    var locationID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mHomeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        ivBack?.setOnClickListener {
            onBackPressed()
        }

        mHomeViewModel.mScrapLocationData.observe(this) {
            hideLoading()
            when (it) {
                is com.slt.base.Result.Success -> {
//                    makeToast(it.message)
                    it.data.let {
                        Constants.scrapLocationModel = it
                        val intent = Intent(this,NewScrapActivity::class.java)
                        intent.putExtra(Constants.LOCATIONID,locationID)
                        startActivity(intent)
                    }
                }
                is com.slt.base.Result.Error -> {
                    makeException(it.code, it.message, mainView)
//                    makeToast(it.message)
                }
                is com.slt.base.Result.ErrorCode -> {
                    makeToast(it.message)
                }
                is com.slt.base.Result.SuccessWithMessage -> {
                    makeToast(it.message)
                }
                else -> {}
            }
        }

    }

    override fun handleResult(rawResult: Result?) {

        locationID = rawResult?.text.toString()
        mHomeViewModel.scrapFetchLocation(rawResult?.text.toString()).apply {
            showLoading()
        }
//        val returnIntent = Intent()
//        returnIntent.putExtra("data", rawResult?.text)
//        setResult(RESULT_OK, returnIntent)
//        finish()
    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this) // Register ourselves as a handler for scan results.//////////////////////////////////////
        mScannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera() // Stop camera on pause

    }
}