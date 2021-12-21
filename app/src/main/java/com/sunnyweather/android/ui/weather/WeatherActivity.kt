package com.sunnyweather.android.ui.weather

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.getSky
import com.sunnyweather.android.logic.model.weatherModel.Weather
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import kotlinx.android.synthetic.main.now.placeName
import kotlinx.android.synthetic.main.place_item.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 这里的内容主要对应搜索城市信息出来的RecycleView中的子项点击跳转过来
 */
class WeatherActivity : AppCompatActivity() {
    val viewModel by lazy {
        ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }

    companion object {
        fun startAction(
            context: Context,
            location_lng: String,
            location_lat: String,
            place_name: String
        ) {
            var intent = Intent(context, WeatherActivity::class.java)
            intent.putExtra("location_lng", location_lng)
            intent.putExtra("location_lat", location_lat)
            intent.putExtra("place_name", place_name)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //将背景图和状态栏融合起来
        var decorView = window.decorView
        //改变系统UI，表示Activity的UI会显示在状态栏上
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //将状态栏修改为透明
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_weather)
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this) { result ->
            var weather = result.getOrNull()

            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        }
        //先添加监听再修改数据
        viewModel.refreshLocation(viewModel.locationLng, viewModel.locationLat)
    }

    private fun showWeatherInfo(weather: Weather) {
        placeName.text = viewModel.placeName
        var realtime = weather.realtime
        var daily = weather.daily
        //填充now.xml布局中的数据
        var currentTempText = "${realtime.temperature.toInt()} ℃"
        currentTemperature.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info
        var currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        //填充forecast.xml的布局
        forecastLayout.removeAllViews()
        var days = daily.skycon.size
        for (i in 0 until days) {
            var skycon = daily.skycon[i]
            var temperature = daily.temperature[i]
            //往forecast.xml布局中加入forecast_item布局子项(构建一个view加入forecast布局中)
            var view =
                LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)
            var dateInfo = view.findViewById(R.id.dateInfo) as TextView
            var skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            var skyInfo = view.findViewById(R.id.skyInfo) as TextView
            var temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            var sky = getSky(skycon.value)
            skyInfo.text = sky.info
            skyIcon.setImageResource(sky.icon)
            val temperatureText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = temperatureText
            forecastLayout.addView(view)
        }
        //填充life_index.xml中的数据
        var lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE
    }
}