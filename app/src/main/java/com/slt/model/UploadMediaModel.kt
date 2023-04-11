package com.slt.model
import com.google.gson.annotations.SerializedName


data class UploadMediaModel(
    @SerializedName("mediaID")
    val mediaID: String
)