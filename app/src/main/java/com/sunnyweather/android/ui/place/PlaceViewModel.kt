package com.sunnyweather.android.ui.place

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place

/**
 * 在这里我们需要监听的是在输入栏中输入的内容
 */
class PlaceViewModel : ViewModel() {
    //这个数据会被 20行处监听，一旦发生修改就会生成一个placeLiveData（另外一个可监听数据）
    private val searchLiveData = MutableLiveData<String>()

    //对界面上的城市数据进行缓存，确保在手机屏幕旋转时数据不会丢失
    val placeList = ArrayList<Place>()

    //将仓库层Repository返回的LiveData转换曾可供Activity层观察的LiveData对象
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    //每当执行searchPlaces()对数据进行修改，switchMap中的转换函数就会得到执行
    fun searchPlaces(query: String) {
        Log.d("PlaceViewModel", "searchLiveData change")
        searchLiveData.value = query
    }

    fun savePlace(place: Place) = Repository.savePlace(place)
    fun getPlace(): Place = Repository.getPlace()
    fun isPlaceSaved(): Boolean = Repository.isPlaceSaved()
}