package com.slt.data.remote

import com.google.gson.JsonObject
import com.slt.base.BaseViewModel
import com.slt.data.datamanager.DataManager
import com.slt.data.preferences.PreferenceManager
import com.slt.model.HistoryModel
import com.slt.model.LoginModel
import com.slt.model.ScrapLocationModel
import com.slt.model.UploadMediaModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("application/ping")
    suspend fun ping(
    ): Response<ApiResponse<Any>>

    @POST("account/login")
    suspend fun userLogin(
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<LoginModel>>

    @POST("account/logout")
    suspend fun logout(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
    ): Response<ApiResponse<Any>>

    @POST("scrap/fetchLocation")
    suspend fun scrapFetchLocation(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<ScrapLocationModel>>

    @POST("scrap/uploadMedia")
    suspend fun uploadMedia(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<UploadMediaModel>>

    @POST("scrap/submit")
    suspend fun submit(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<Any>>

    @POST("scrap/fetchHistory")
    suspend fun fetchHistory(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<ArrayList<HistoryModel>>>

    /*@POST("application/install")
    suspend fun install(
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<InstallModel>>

    @POST("account/userExist")
    suspend fun userExist(
        @Header(BaseViewModel.PARAM_DEVICEID) deviceId: String,
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<UserExistModel>>

    @POST("account/register")
    suspend fun userRegister(
        @Header(BaseViewModel.PARAM_DEVICEID) deviceId: String,
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<UserExistModel>>

    @POST("account/login")
    suspend fun userLogin(
        @Header(BaseViewModel.PARAM_DEVICEID) deviceId: String,
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<RegisterrModel>>

    @POST("member/availablePoints")
    suspend fun availablePoints(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
    ): Response<ApiResponse<Any>>

    @POST("contest/getList")
    suspend fun getList(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<ArrayList<HomeModel>>>

    @POST("contest/getInfo")
    suspend fun getContestInfo(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<ContestInfoModel>>

    @POST("contest/participate")
    suspend fun getContestParticipate(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<ContestParticipateModel>>

    @POST("contest/getParticipationDetails")
    suspend fun getParticipationDetails(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<ArrayList<OrderConfirmModel>>>

    @POST("member/sendSuggestion")
    suspend fun sendSuggestion(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<Any>>

    @POST("member/removeProfileImage")
    suspend fun removeImage(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY)
    ): Response<ApiResponse<Any>>

    @POST("member/setProfileImage")
    suspend fun setProfileImage(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<Any>>

    @POST("member/updateDetails")
    suspend fun updateDetails(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonArray
    ): Response<ApiResponse<Any>>

    @POST("member/generateQRcode")
    suspend fun generateQRCode(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY)

    ): Response<ApiResponse<Any>>

    @POST("transactions/earnPoints")
    suspend fun earnPoint(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<EarnPointModel>>

    @POST("partners/fetch")
    suspend fun partnerEarnPointFetch(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
    ): Response<ApiResponse<ArrayList<EarnMorePointModelItem>>>

    @POST("partners/getDetails")
    suspend fun earnPointDetail(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<EarnMorePointDetailsModel>>

    @POST("transactions/fetch")
    suspend fun TransactionFetch(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
    ): Response<ApiResponse<ArrayList<TransactionHistoryModelItem>>>

    @POST("transactions/overview")
    suspend fun TransactionOverview(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
    ): Response<ApiResponse<EarnPointOverviewModel>>

    @POST("application/contest")
    suspend fun applicationContestWebView(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
    ): Response<ApiResponse<Any>>

    @POST("contest/getInfo")
    suspend fun getAnnounceInfo(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<AnnounceModel>>

    @POST("member/getDetails")
    suspend fun getProfileDetail(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
    ): Response<ApiResponse<ProfileModel>>

    @POST("application/pincodeDetails")
    suspend fun pincodeDetails(
        @Header(BaseViewModel.PARAM_TOKENKEY) tokenKey: String = DataManager.getInstance().getPreference().getString(
            PreferenceManager.LOGIN_KEY),
        @Body jsonObject: JsonObject
    ): Response<ApiResponse<CityModel>>*/
}