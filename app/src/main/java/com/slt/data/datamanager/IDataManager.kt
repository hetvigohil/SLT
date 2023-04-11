package com.slt.data.datamanager

import com.slt.data.preferences.IPreference
import com.slt.data.remote.ApiService
import com.slt.data.roomdatabase.AppDatabase

interface IDataManager {
    fun getPreference(): IPreference

        fun getDatabase(): AppDatabase
    fun getRemote(type: Int): ApiService
}