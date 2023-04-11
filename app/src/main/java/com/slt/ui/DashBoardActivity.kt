package com.slt.ui

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.slt.R
import com.slt.base.BaseActivity
import com.slt.data.datamanager.DataManager
import com.slt.data.preferences.PreferenceManager
import com.slt.extensions.makeException
import com.slt.ui.adapter.RecentCollectionAdapter
import com.slt.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_dash_board.*

class DashBoardActivity : BaseActivity(R.layout.activity_dash_board) {

    lateinit var recentCollectionAdapter : RecentCollectionAdapter
    lateinit var homeViewModel : HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        tvUserName.text = DataManager.getInstance().getPreference().getString(PreferenceManager.USER_NAME, "")

        rvRecentCollection.layoutManager = LinearLayoutManager(this)
        recentCollectionAdapter = RecentCollectionAdapter()
        rvRecentCollection.adapter = recentCollectionAdapter

        cardViewNewScrap.setOnClickListener {
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.CAMERA
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            startActivity(Intent(this@DashBoardActivity,ScannerActivity::class.java))
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) { /* ... */
                    }
                }).onSameThread().check()
        }
        homeViewModel.historyData().apply {
            showLoading()
        }

        ivLogout.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Logout")
            builder.setMessage("ARe you sure want to logout?")

            builder.setPositiveButton("Yes") { dialog, which ->
                homeViewModel.logout().apply {
                    showLoading()
                }
            }

            builder.setNegativeButton("No") { dialog, which ->
            }

            builder.show()

        }

        homeViewModel.mLogout.observe(this) {
            hideLoading()
            when (it) {
                is com.slt.base.Result.Success -> {
                    makeToast(it.message)
                    it.data.let {
                        startActivity(Intent(this,SplashActivity::class.java))
                        finishAffinity()
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


        homeViewModel.mHistoryData.observe(this) {
            hideLoading()
            when (it) {
                is com.slt.base.Result.Success -> {
                    makeToast(it.message)
                    recentCollectionAdapter.addData(it.data)
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
}