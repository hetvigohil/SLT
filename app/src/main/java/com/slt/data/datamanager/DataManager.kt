package com.slt.data.datamanager

import com.slt.data.remote.ApiClient
import com.slt.data.remote.ApiService
import com.slt.extra.Constants
import com.slt.data.preferences.PreferenceManager
import com.slt.data.roomdatabase.AppDatabase

class DataManager : IDataManager {

    companion object {
        private var SINGLETON_INSTANCE: IDataManager? = null
        fun getInstance(): IDataManager {
            if (SINGLETON_INSTANCE == null) {
                SINGLETON_INSTANCE = DataManager()
            }
            return SINGLETON_INSTANCE!!
        }

        private const val mBaseUrl: String = "https://stl-scm.atquarter.com/api.andriod/"
//        private const val mBaseUrl: String = "http://192.168.0.171/api.club/"
    }

    override fun getPreference() = PreferenceManager.getInstance()

    override fun getDatabase(): AppDatabase {
        return AppDatabase.getDatabase()
    }

    override fun getRemote(type: Int): ApiService {
        val baseUrl: String = when (type) {
            Constants.TYPE_BASE -> mBaseUrl
            else -> mBaseUrl
        }
        return ApiClient.getInstance(baseUrl).create(ApiService::class.java)
    }
}