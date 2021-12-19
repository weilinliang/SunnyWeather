package com.sunnyweather.android.logic.network.weatherNetwork

import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.weatherModel.DailyResponse
import com.sunnyweather.android.logic.model.weatherModel.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 访问天气信息API的Retrofit接口
 */
interface WeatherService {
    //获取实时天气信息
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<RealtimeResponse>

    //获取未来几天的天气情况
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<DailyResponse>
}