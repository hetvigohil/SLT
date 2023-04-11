package com.slt.data.roomdatabase.userdata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class UserDataItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "memberID") var memberID: String = "",
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "emailID") var emailId: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @ColumnInfo(name = "mobileNo") var mobileNo: String = "",
    @ColumnInfo(name = "clubCard") var clubCard: String = "",
    @ColumnInfo(name = "address") var address: String = "",
    @ColumnInfo(name = "availablePoints") var availablePoints: Long = 0L
)