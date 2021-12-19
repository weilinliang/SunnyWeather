package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place

class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    //对界面上的城市数据进行缓存，确保在手机屏幕旋转时数据不会丢失
    val placeList = ArrayList<Place>()

    //将仓库层Repository返回的LiveData转换曾可供Activity层观察的LiveData对象
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    //每当执行searchPlaces()对数据进行修改，switchMap中的转换函数就会得到执行
    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }
}