package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

/**
 * 仓库层：主要判断请求数据是从本地数据源（缓存？）中获取还是从网络数据源中获取
 *          并将获取的数据返回给调用方
 */
object Repository {
    //因为是查询城市数据，所以在这里没必要进行缓存
    //Result 是Kotlin中的内置处理数据的类
    //Dispatchers.IO指定liveData方法线程参数类型，Android不允许在主线程中进行网络请求，都数据库什么的
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            var response = SunnyWeatherNetwork.searchPlaces(query)
            if (response.status == "ok") {
                var places = response.places
                //通过success包装数据
                Result.success(places)
            } else {
                //failure包装一个异常
                Result.failure(RuntimeException("response status is ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        //将包装的结果发出去，类似LiveData中通过setValue监听数据变化
        emit(result)
    }
}