package com.slt.data.remote

import com.slt.base.BaseApplication
import com.google.gson.Gson
import com.slt.extensions.dismissSnackBar
import com.slt.extra.Constants
import com.slt.base.Result
import retrofit2.Response
import java.net.UnknownHostException

class ApiResponseHandle<T : Any> {
    fun responseHttp(response: Response<ApiResponse<T>>): Result<T> {
        if (response.isSuccessful) {
            BaseApplication.SINGLETON_INSTANCE.dismissSnackBar(true)
            response.body()?.output?.let { data ->
                return Result.Success(data, response.body()!!.message,response.body()!!.status
                )
            } ?: kotlin.run {
                response.body()?.let {
                    return Result.SuccessWithMessage(it.status, it.message,it.status)
                } ?: kotlin.run {
                    return Result.Error(
                        IllegalStateException(response.message()),
                        response.message(),
                        code = response.code()
                    )
                }
            }
        } else {
            response.errorBody()?.let { errorBody ->
                val item = Gson().fromJson(errorBody.string(), ApiResponse::class.java)
                return if (!item.status) {
                    Result.Error(
                        IllegalStateException(response.message()),
                        response.message(),
                        code = response.code()
                    )
                } else {
                    Result.ErrorCode(
                        IllegalStateException(item.message),
                        item.message,
                        code = item.status
                    )
                }
            } ?: kotlin.run {
                return Result.Error(IllegalStateException(response.message()))
            }
        }
    }

    fun internetServerError(e: Exception): Result<T>? {
        return if (e is UnknownHostException)
            Result.Error(IllegalStateException(""), "", code = Constants.NO_INTERNET)
        else
            Result.Error(e)
    }
}