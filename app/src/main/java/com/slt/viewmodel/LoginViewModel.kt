package com.slt.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.slt.base.BaseViewModel
import com.slt.data.preferences.PreferenceManager
import com.slt.data.remote.ApiResponseHandle
import com.slt.model.LoginModel
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {

    val mPing = MutableLiveData<com.slt.base.Result<Any>>()
    val mLoginData = MutableLiveData<com.slt.base.Result<LoginModel>>()

    fun ping() {
        viewModelScope.launch {
            try {
                val response = getRemote().ping()
                mPing.value = ApiResponseHandle<Any>().responseHttp(response)
            } catch (e: Exception) {
                mPing.value = ApiResponseHandle<Any>().internetServerError(e)
            }
        }
    }

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            try {
                val response = getRemote().userLogin(
                    jsonObject = getJsonProperty(
                        Pair(
                            PARAM_USERNAME, userName
                        ),
                        Pair(PARAM_PASSWORD, password)
                    ),
                )
                if (response.isSuccessful) {
                    response.body()?.output?.let {
                        getPreference().putString(
                            PreferenceManager.ISLOGIN,
                            PreferenceManager.ISLOGIN
                        )
                        getPreference().putString(PreferenceManager.LOGIN_KEY, it.tokenKey)
                        getPreference().putString(PreferenceManager.USER_NAME, it.name)
                    }
                }
                mLoginData.value = ApiResponseHandle<LoginModel>().responseHttp(response)
            } catch (e: Exception) {
                mLoginData.value = ApiResponseHandle<LoginModel>().internetServerError(e)
            }
        }
    }
}