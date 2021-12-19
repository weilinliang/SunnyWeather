package com.sunnyweather.android.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Location

/**
 * 在这里我们需要监听的是经纬度数据
 * 三个变量是保存数据，为的是在旋转屏幕时还保留着之前的缓存
 */
class WeatherViewModel : ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    //locationLiveData数据发生变化，会返回一个可监听的weather数据weatherLiveData（在View层中加入监听）
    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    //向外暴露修改经纬度locationLiveData的方法，同时会触发switchMap中的转换逻辑
    fun refreshLocation(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }
}