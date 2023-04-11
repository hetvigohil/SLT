package com.slt.model
import com.google.gson.annotations.SerializedName


data class LoginModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("tokenKey")
    val tokenKey: String="",
)