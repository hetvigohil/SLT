package com.slt.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.slt.R
import com.slt.base.BaseActivity
import com.slt.extensions.makeException
import com.slt.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(R.layout.activity_login) {

    private lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        mLoginViewModel.ping()

        rlLogin.setOnClickListener {

            if (edtUserName.text.toString().trim().isEmpty()){
                Toast.makeText(this,"Enter Username",Toast.LENGTH_SHORT).show()
            }else if (edtPassword.text.toString().trim().isEmpty()){
                Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show()
            }else{
                mLoginViewModel.login(edtUserName.text.toString(),edtPassword.text.toString()).apply {
                    showLoading()
                }
            }
        }

        mLoginViewModel.mLoginData.observe(this) {
            hideLoading()
            when (it) {
                is com.slt.base.Result.Success -> {
                    makeToast(it.message)
                    it.data.let {
                        startActivity(Intent(this,DashBoardActivity::class.java))
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


    }
}