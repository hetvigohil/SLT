package com.slt.data.roomdatabase.userdata

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDataDao {

    /*@Query("SELECT * FROM user_data")
    suspend fun getAllSavedUserDetail(): UserDataItem

    @Insert
    suspend fun insertUserDetail(userDataItem: UserDataItem)

    @Update
    suspend fun updateUserDetail(userDataItem: UserDataItem)*/

}