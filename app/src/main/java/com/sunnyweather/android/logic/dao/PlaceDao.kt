package com.sunnyweather.android.logic.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Place

/**
 * 相当于访问数据库啊！DAO :Data Access Object
 *
 * @author weilinliang 2021/12/21
 */
object PlaceDao {
    private const val TAG = "PlaceDao"
    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getPlace(): Place {
        var placeGson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeGson, Place::class.java)
    }

    private fun sharedPreferences(): SharedPreferences {
        return SunnyWeatherApplication.context
            .getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)
    }

    fun isPlaceSave(): Boolean {
        return sharedPreferences().contains("place")
    }
}