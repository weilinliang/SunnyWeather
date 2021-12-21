package com.sunnyweather.android

import android.app.Application
import android.content.Context

/**
 * 设置全局的context
 */
class SunnyWeatherApplication : Application() {
    companion object {
        //    TODO : 此处填入申请的彩云接口token
        const val TOKEN = "T8kgUkONj7lY6KrP"
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}