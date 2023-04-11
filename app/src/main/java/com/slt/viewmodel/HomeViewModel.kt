package com.slt.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.slt.base.BaseViewModel
import com.slt.data.remote.ApiResponseHandle
import com.slt.model.HistoryModel
import com.slt.model.ScrapLocationModel
import com.slt.model.UploadMediaModel
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {

    val mLogout = MutableLiveData<com.slt.base.Result<Any>>()
    val mScrapLocationData = MutableLiveData<com.slt.base.Result<ScrapLocationModel>>()
    val mUploadMediaData = MutableLiveData<com.slt.base.Result<UploadMediaModel>>()
    val mSubmitData = MutableLiveData<com.slt.base.Result<Any>>()
    val mHistoryData = MutableLiveData<com.slt.base.Result<ArrayList<HistoryModel>>>()

    fun logout() {
        viewModelScope.launch {
            try {
                val response = getRemote().logout()
                mLogout.value = ApiResponseHandle<Any>().responseHttp(response)
            } catch (e: Exception) {
                mLogout.value = ApiResponseHandle<Any>().internetServerError(e)
            }
        }
    }

    fun scrapFetchLocation(locationId: String) {
        viewModelScope.launch {
            try {
                val response = getRemote().scrapFetchLocation(
                    jsonObject = getJsonProperty(
                        Pair(
                            PARAM_LOCATIONID, locationId
                        )
                    ),
                )
                mScrapLocationData.value = ApiResponseHandle<ScrapLocationModel>().responseHttp(response)
            } catch (e: Exception) {
                mScrapLocationData.value = ApiResponseHandle<ScrapLocationModel>().internetServerError(e)
            }
        }
    }

    fun uploadMedia(base64Img:String,fileName:String,fileFormat:String) {
        viewModelScope.launch {
            try {
                val response = getRemote().uploadMedia(
                    jsonObject = getJsonProperty(
                        Pair(PARAM_FILE, base64Img),
                        Pair(PARAM_FILETYPE, "image"),
                        Pair(PARAM_FILENAME, fileName),
                        Pair(PARAM_FILEFORMAT, fileFormat)
                    )
                )
                mUploadMediaData.value =
                    ApiResponseHandle<UploadMediaModel>().responseHttp(response)
            } catch (e: Exception) {
                mUploadMediaData.value =
                    ApiResponseHandle<UploadMediaModel>().internetServerError(e)
            }
        }
    }

    fun submit(jsonArray: JsonObject) {
        viewModelScope.launch {
            try {
                val response = getRemote().submit(jsonObject = jsonArray)
                mSubmitData.value =
                    ApiResponseHandle<Any>().responseHttp(response)
            } catch (e: Exception) {
                mSubmitData.value =
                    ApiResponseHandle<Any>().internetServerError(e)
            }
        }
    }

    fun historyData() {
        viewModelScope.launch {
            try {
                val response = getRemote().fetchHistory(
                    jsonObject = getJsonProperty(
                        Pair(PARAM_DATE, "2023-04-02"),
                    )
                )
                mHistoryData.value = ApiResponseHandle<ArrayList<HistoryModel>>().responseHttp(response)
            } catch (e: Exception) {
                mHistoryData.value = ApiResponseHandle<ArrayList<HistoryModel>>().internetServerError(e)
            }
        }
    }
}