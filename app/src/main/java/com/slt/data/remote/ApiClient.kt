package com.slt.data.remote

import com.google.gson.GsonBuilder
import com.slt.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {
        private const val NETWORK_READ_TIME_OUT = 10L
        private const val NETWORK_CONNECT_TIME_OUT = 10L

        private var SINGLETON_INSTANCE: Retrofit? = null

        fun getInstance(baseUrl: String): Retrofit {
            if (SINGLETON_INSTANCE == null) {
                return Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder().setLenient().create()
                        )
                    )
                    .client(getHttpClient()).build()
            }
            return SINGLETON_INSTANCE!!
        }

        private fun getHttpClient(): OkHttpClient {
            val httpClient = OkHttpClient.Builder()
            httpClient.readTimeout(NETWORK_READ_TIME_OUT, TimeUnit.MINUTES)
            httpClient.connectTimeout(NETWORK_CONNECT_TIME_OUT, TimeUnit.MINUTES)

            httpClient.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original: Request = chain.request()
                    val requestBuilder: Request.Builder = original.newBuilder()
                        .addHeader("app_version", BuildConfig.VERSION_NAME)
                        .addHeader("os_type", "android")
                        .addHeader("os_version", android.os.Build.VERSION.RELEASE)
                        .addHeader("developerKey","TJI9hhjdUpLzmluJsCDJb3uJ0HF8O5X7")
                    val request: Request = requestBuilder.build()
                    return chain.proceed(request)
                }
            })
            /**
             * Add Logging interceptors for see the API response in debug application
             */
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(logging)
            }
            return httpClient.build()
        }

        /**
         * Method used to disconnect the previous server when user disconnect the app
         */
        fun setDefaultUrl() {
            SINGLETON_INSTANCE = null
        }
    }
}