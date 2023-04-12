package com.slt.base

import androidx.lifecycle.ViewModel
import com.slt.data.preferences.IPreference
import com.slt.data.remote.ApiService
import com.slt.extra.Constants
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.slt.data.datamanager.DataManager

open class BaseViewModel : ViewModel() {

    companion object {
        const val PARAM_PASSWORD = "password"
        const val PARAM_USERNAME = "username"
        const val PARAM_TOKENKEY = "tokenKey"
        const val PARAM_LOCATIONID = "locationID"
        const val PARAM_FILE = "file"
        const val PARAM_FILETYPE = "fileType"
        const val PARAM_FILENAME = "fileName"
        const val PARAM_FILEFORMAT = "fileFormat"
        const val PARAM_ITEM = "item"
        const val PARAM_DATE = "date"

        /**
         * Method is used to add all properties into JSON one by one
         * @param requestParams is Pair<Key,Value> that hold all the request parameters
         */
        fun getJsonProperty(vararg requestParams: Pair<String, Any?>): JsonObject {
            return JsonObject().apply {
                for (item in requestParams) {
                    val convertedValue = item.second.toString()
                    when (item.second) {
                        is Int -> addProperty(item.first, convertedValue.toInt())
                        is Long -> addProperty(item.first, convertedValue.toLong())
                        is Float -> addProperty(item.first, convertedValue.toFloat())
                        is Double -> addProperty(item.first, convertedValue.toDouble())
                        is Boolean -> addProperty(item.first, convertedValue.toBoolean())
                        is JsonArray -> add(item.first, item.second as JsonArray)
                        is JsonObject -> add(item.first, item.second as JsonObject)
                        else -> addProperty(item.first, convertedValue)
                    }
                }
            }
        }
    }

    fun getPreference(): IPreference {
        return DataManager.getInstance().getPreference()
    }

//    fun getDatabase(): AppDatabase {
//        return DataManager.getInstance().getDatabase()
//    }

    fun getRemote(type: Int = Constants.TYPE_BASE): ApiService {
        return DataManager.getInstance().getRemote(type)
    }

}