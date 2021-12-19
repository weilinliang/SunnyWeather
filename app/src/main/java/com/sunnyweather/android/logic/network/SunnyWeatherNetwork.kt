package com.sunnyweather.android.logic.network

import com.sunnyweather.android.logic.network.placeNetwork.PlaceService
import com.sunnyweather.android.logic.network.weatherNetwork.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.ln

/**
 * 统一的数据源访问入口
 * 对所有网络请求API进行封装
 */
object SunnyWeatherNetwork {
    private val placeService = ServiceCreator.create<PlaceService>()
    private val weatherService = ServiceCreator.create<WeatherService>()

    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()
    
    suspend fun searchDailyWeather(lng: String, lat: String) =
        weatherService.getDailyWeather(lng, lat).await()

    suspend fun searchRealtimeWeather(lng: String, lat: String) =
        weatherService.getRealtimeWeather(lng, lat).await()

    //这样就不用每次去访问网络请求都需要自己写一个回调了？
    //自定义一个扩展函数await(async中的await功能一样的？)，将searchPlaces声明为挂起函数
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    var body = response.body()
                    if (body != null) {
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(RuntimeException("response is empty"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}