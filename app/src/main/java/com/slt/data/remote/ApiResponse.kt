package com.slt.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiResponse<T> {

    @Expose
    @SerializedName(value = "status")
    var status: Boolean = false

    @Expose
    @SerializedName(value = "message")
    var message: String = ""

    @Expose
    @SerializedName(value = "output")
    var output: T? = null

}