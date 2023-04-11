package com.slt.model
import android.graphics.Bitmap
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScrapLocationModel(
    @SerializedName("availableItem")
    val availableItem: ArrayList<AvailableItem>,
    @SerializedName("Location")
    val location: String,
    @SerializedName("Zone")
    val zone: String
): Parcelable
@Parcelize
data class AvailableItem(
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    var mediaId : String = "",
    var imagePath : Bitmap ?=null,
    var filePath : String = ""
): Parcelable