package com.slt.base

sealed class Result<out T : Any> {
    class SuccessWithMessage<out T : Any>(val code: Boolean, val message: String = "",val status:Boolean) : Result<T>()
    class Success<out T : Any>(val data: T, val message: String = "",val status:Boolean,) : Result<T>()
    class Error(val exception: Exception, val message: String = exception.localizedMessage!!, val code: Int = 500) :
            Result<Nothing>()
    class ErrorCode(val exception: Exception, val message: String = exception.localizedMessage!!, val code: Boolean = false) :
        Result<Nothing>()
}