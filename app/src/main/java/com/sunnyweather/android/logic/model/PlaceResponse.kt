package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

/**
 * 在这个文件中定义数据类
 * 按照搜索城市数据接口返回的JSON数据进行定义
 */

data class PlaceResponse(val status: String, val places: List<Place>)

//SerializedName使得JSON字段的和Kotlin字段之间建立映射关系
data class Place(
    val name: String, val location: Location,
    @SerializedName("formatted_address") val address: String
)

data class Location(val lng: String, val lat: String)

