package com.slt.base

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.multidex.MultiDex


class BaseApplication : Application() {
    companion object {
        @get:Synchronized
        lateinit var SINGLETON_INSTANCE: BaseApplication
        var isAppInForeground : Boolean = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        //App in background
        isAppInForeground = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        //App in foreground
        isAppInForeground = true
    }

    override fun onCreate() {
        super.onCreate()
        SINGLETON_INSTANCE = this
        MultiDex.install(this)
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }else{
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }

            }

            override fun onActivityResumed(activity: Activity) {
            }
        })
    }
}