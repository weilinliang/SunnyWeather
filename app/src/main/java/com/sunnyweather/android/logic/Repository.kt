package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.weatherModel.DailyResponse
import com.sunnyweather.android.logic.model.weatherModel.Weather
import com.sunnyweather.android.logic.model.weatherModel.dailyResponseMock
import com.sunnyweather.android.logic.model.weatherModel.realtimeResponseMock
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 * 仓库层：主要判断请求数据是从本地数据源（缓存？）中获取还是从网络数据源中获取
 *          并将获取的数据返回给调用方
 */
object Repository {
    //使用高阶函数将查询函数中重复的try catch块 和 emit抽出来
    fun <T> samePart(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData(context) {
            var result = try {
                block.invoke()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

    /**
     * 因为是查询城市数据，所以在这里没必要进行缓存
     * Result 是Kotlin中的内置处理数据的类(会自动的包装成一个泛型类？这里后面括号外面的高阶函数并没有返回值啊！?)
     * Dispatchers.IO指定liveData方法线程参数类型，Android不允许在主线程中进行网络请求，都数据库什么的
     * 返回的数据应该是LiveData<List<Place>>
     */
    fun searchPlaces(query: String) = samePart(Dispatchers.IO) {
        var response = SunnyWeatherNetwork.searchPlaces(query)
        if (response.status == "ok") {
            var places = response.places
            //通过success包装数据
            Result.success(places)
        } else {
            //failure包装一个异常
            Result.failure(RuntimeException("response status is ${response.status}"))
        }
    }

    /**
     * 返回的数据应该是LiveData<Weather>
     */
    fun refreshWeather(lng: String, lat: String) = samePart(Dispatchers.IO) {
        //创建协程环境
        coroutineScope {
            var deferredDaily = async {
                SunnyWeatherNetwork.searchDailyWeather(lng, lat)
            }
            var deferredRealtime = async {
                SunnyWeatherNetwork.searchRealtimeWeather(lng, lat)
            }
            var dailyResponse = deferredDaily.await()
            var realtimeResponse = deferredRealtime.await()
            //模拟数据
//            var dailyResponse = dailyResponseMock
//            var realtimeResponse = realtimeResponseMock

            if (dailyResponse.status == "ok" && realtimeResponse.status == "ok") {
                var weather =
                    Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

//    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
//        val result = try {
//            var response = SunnyWeatherNetwork.searchPlaces(query)
//            if (response.status == "ok") {
//                var places = response.places
//                //通过success包装数据
//                Result.success(places)
//            } else {
//                //failure包装一个异常
//                Result.failure(RuntimeException("response status is ${response.status}"))
//            }
//        } catch (e: Exception) {
//            Result.failure<List<Place>>(e)
//        }
//        //将包装的结果发出去，类似LiveData中通过setValue监听数据变化
//        emit(result)
//    }
//
//    fun refreshWeather(lng: String, lat: String) = liveData(Dispatchers.IO) {
//        val result = try {
//            //创建协程环境
//            coroutineScope {
//                var deferredDaily = async {
//                    SunnyWeatherNetwork.searchDailyWeather(lng, lat)
//                }
//                var deferredRealtime = async {
//                    SunnyWeatherNetwork.searchRealtimeWeather(lng, lat)
//                }
//                var dailyResponse = deferredDaily.await()
//                var realtimeResponse = deferredRealtime.await()
//                if (dailyResponse.status == "ok" && realtimeResponse.status == "ok") {
//                    var weather =
//                        Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
//                    Result.success(weather)
//                } else {
//                    Result.failure(
//                        RuntimeException(
//                            "realtime response status is ${realtimeResponse.status}" +
//                                    "daily response status is ${dailyResponse.status}"
//                        )
//                    )
//                }
//            }
//        } catch (e: Exception) {
//            Result.failure<Weather>(e)
//        }
//        emit(result)
//    }
}