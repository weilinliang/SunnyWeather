package com.sunnyweather.android.logic.model.weatherModel

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.math.min

/**
 * 获取未来几天天气信息数据
 */
data class DailyResponse(val status: String, val result: Result) {
    data class Result(val daily: Daily)
    data class Daily(
        val temperature: List<Temperature>,
        val skycon: List<Skycon>,
        @SerializedName("life_index") val lifeIndex: LifeIndex
    )

    data class Temperature(val min: Float, val max: Float)
    data class Skycon(val value: String, val date: Date)
    data class LifeIndex(
        val coldRisk: List<LifeDescription>,
        val carWashing: List<LifeDescription>,
        val ultraviolet: List<LifeDescription>,
        val dressing: List<LifeDescription>
    )

    data class LifeDescription(val desc: String)
}

val dailyResponseMock = DailyResponse(
    "ok", DailyResponse.Result(
        DailyResponse.Daily(
            arrayListOf(
                DailyResponse.Temperature(11.7f, 22.6f),
                DailyResponse.Temperature(77.7f, 99.9f),
                DailyResponse.Temperature(-15.0f, 1.2f)
            ),
            arrayListOf(
                DailyResponse.Skycon("HEAVY_RAIN", Date()),
                DailyResponse.Skycon("STORM_SNOW", Date()),
                DailyResponse.Skycon("MODERATE_HAZE", Date())
            ),
            DailyResponse.LifeIndex(
                arrayListOf(DailyResponse.LifeDescription("易发")),
                arrayListOf(DailyResponse.LifeDescription("事宜")),
                arrayListOf(DailyResponse.LifeDescription("无")),
                arrayListOf(DailyResponse.LifeDescription("舒适"))
            )
        )
    )
)