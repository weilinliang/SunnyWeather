package com.sunnyweather.android

import android.app.Application
import android.content.Context

/**
 * 设置全局的context
 */
class SunnyWeatherApplication : Application() {
    companion object {
        //    TODO : 此处填入申请的彩云接口token
        const val TOKEN = ""
        lateinit var instance: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = applicationContext
    }
}