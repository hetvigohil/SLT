package com.slt.model
import android.graphics.Bitmap
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScrapLocationModel(
    @SerializedName("location")
    val location: String = "",
    @SerializedName("zone")
    val zone: String="",
    @SerializedName("questionText")
    val questionText: String="",
    @SerializedName("availableItem")
    val availableItem: ArrayList<AvailableItem> ?= null,

): Parcelable
@Parcelize
data class AvailableItem(
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("mediaId")
    var mediaId : String ?= "",
    @SerializedName("filePath")
    var filePath : String ?= "",
    @SerializedName("itemImage")
    var itemImage : String ?= ""
): Parcelable