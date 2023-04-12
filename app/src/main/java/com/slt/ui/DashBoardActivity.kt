package com.slt.ui

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
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
import com.slt.extra.Constants
import com.slt.ui.adapter.RecentCollectionAdapter
import com.slt.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_dash_board.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashBoardActivity : BaseActivity(R.layout.activity_dash_board) {

    lateinit var recentCollectionAdapter: RecentCollectionAdapter
    lateinit var homeViewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        tvUserName.text =
            DataManager.getInstance().getPreference().getString(PreferenceManager.USER_NAME, "")

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
                            startActivity(
                                Intent(
                                    this@DashBoardActivity,
                                    ScannerActivity::class.java
                                )
                            )
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

        ivLogout.setOnClickListener {

            /* logoutDialog(this){

             }*/

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
                        GlobalScope.launch {
                            DataManager.getInstance().getDatabase().clearAllTables()
                        }
                        DataManager.getInstance().getPreference().clearPreference()
                        startActivity(
                            Intent(
                                applicationContext,
                                SplashActivity::class.java
                            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                        finish()
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
            swipeRefresh.isRefreshing = false
            when (it) {
                is com.slt.base.Result.Success -> {
//                    makeToast(it.message)
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

        swipeRefresh.setOnRefreshListener {
            homeViewModel.historyData().apply {
                showLoading()
            }
        }

        recentCollectionAdapter.onClick = { item, pos ->
            Constants.historyMOdel = item.image
            val intent = Intent(this,ImageViewerActivity::class.java)
            startActivity(intent)
        }

    }

    fun logoutDialog(
        context: Context,
        callBack: ((Boolean) -> Unit)
    ) {
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setContentView(R.layout.dialog_logout)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val cancel = dialog.findViewById<TextView>(R.id.tvCancel)
        val logout = dialog.findViewById<TextView>(R.id.tvLogout)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        logout.setOnClickListener {
            callBack.invoke(true)
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.show()
    }
}