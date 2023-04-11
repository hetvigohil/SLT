package com.slt.model
import com.google.gson.annotations.SerializedName

data class HistoryModel(
    @SerializedName("collectedItem")
    val collectedItem: List<CollectedItem>,
    @SerializedName("date")
    val date: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("zone")
    val zone: String
)

data class CollectedItem(
    @SerializedName("code")
    val code: String,
    @SerializedName("image")
    val image: List<String>,
    @SerializedName("name")
    val name: String
)