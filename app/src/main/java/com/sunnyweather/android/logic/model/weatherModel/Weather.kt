package com.sunnyweather.android.logic.model.weatherModel

/**
 * 将DailyResponse和RealtimeResponse返回的数据都封装起来
 */
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)