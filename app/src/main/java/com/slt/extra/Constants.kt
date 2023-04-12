package com.slt.extra

import com.slt.model.HistoryModel
import com.slt.model.ScrapLocationModel

object Constants {

    const val TYPE_BASE: Int = 0

    const val SESSION_EXPIRE: Int = 401
    const val NO_INTERNET: Int = 502
    const val INVALID_REQUEST: Int = 422
    const val NOT_FOUND: Int = 404
    const val TRY_AGAIN: Int = 400

    const val NOTIFY = "notify"
    const val SCRAPITEM = "scrapItem"
    const val LOCATIONID = "locationID"
    var scrapLocationModel : ScrapLocationModel = ScrapLocationModel()
    var historyMOdel : ArrayList<String> = ArrayList()
}