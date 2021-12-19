package com.sunnyweather.android.logic.model.weatherModel

import com.google.gson.annotations.SerializedName

/**
 * 获取实时天气数据的返回JSON数据
 *  根据返回的json数据设计对应的数据类
 */

data class RealtimeResponse(val status: String, val result: Result) {
    data class Result(val realtime: Realtime)
    data class Realtime(
        val skycon: String, val temperature: Float,
        @SerializedName("air_quality") val airQuality: AirQuality
    )

    data class AirQuality(val aqi: AQI)
    data class AQI(val chn: Float)
}

val realtimeResponseMock = RealtimeResponse(
    "ok", RealtimeResponse.Result(
        RealtimeResponse.Realtime(
            "WIND", 77.7f,
            RealtimeResponse.AirQuality(
                RealtimeResponse.AQI(17.0f)
            )
        )
    )
)