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
    //根据接口 通过代理模式 生成对应的代理类
    private val placeService = ServiceCreator.create<PlaceService>()
    private val weatherService = ServiceCreator.create<WeatherService>()

    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    suspend fun searchDailyWeather(lng: String, lat: String) =
        weatherService.getDailyWeather(lng, lat).await()

    suspend fun searchRealtimeWeather(lng: String, lat: String) =
        weatherService.getRealtimeWeather(lng, lat).await()

    //使用协程简化回调的写法
    //这样就不用每次去访问网络请求都需要自己写一个回调了？
    //suspendCoroutine会将所在的协程挂起，然后在一个普通线程中执行suspendCoroutine中的lambda表达式逻辑，
    // 在lambda表达式的参数中有一个continuation参数，通过这个continuation参数，便可以在lambda中恢复resume这个协程
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