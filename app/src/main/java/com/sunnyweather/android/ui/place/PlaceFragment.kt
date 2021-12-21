package com.sunnyweather.android.ui.place

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Location
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.weatherModel.Weather
import com.sunnyweather.android.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.fragment_layout.*

class PlaceFragment : Fragment() {
    val viewModel by lazy {
        ViewModelProviders.of(this).get(PlaceViewModel::class.java)
    }

    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //在onCreateView中加载前面编写的fragment_layout布局
        return inflater.inflate(R.layout.fragment_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //当在具体地点天气界面关闭程序，再次打开会直接来到关闭前地点的天气界面
        if (viewModel.isPlaceSaved()) {
            var place = viewModel.getPlace()
            WeatherActivity.startAction(context!!, place.location.lng, place.location.lat, place.name)
            return
        }
        var layoutManager = LinearLayoutManager(activity)
        recycleView.layoutManager = layoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        recycleView.adapter = adapter
        searchPlaceEdit.addTextChangedListener { text: Editable? ->
            var content = text.toString()
            if (content.isNotEmpty()) {
                //这里会引发searchLiveData数据变化，然后LiveData监听到数据变化就会去网络请求数据，请求得到的结果会放入placeLiveData
                viewModel.searchPlaces(content)
            } else {
                recycleView.visibility = View.GONE
                backgroundImageView.visibility = View.VISIBLE
                //在查询栏中输入空值，将之前的缓存清空（不然会展示出来？）
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        //当网络请求的数据返回placeLiveData中引起变化时
        viewModel.placeLiveData.observe(this, Observer { result ->
            var places = result.getOrNull()
            //当前还没有数据
//            places = arrayListOf<Place>(
//                Place("北京", Location("北纬77.77", "东经43.96"), "China"),
//                Place("南京", Location("南纬77.7", "西经43.96"), "China"),
//                Place("东京", Location("北纬45.63", "东经45.12"), "Japan"),
//            )
            if (places != null) {
                recycleView.visibility = View.VISIBLE
                backgroundImageView.visibility = View.GONE
                //替换缓存
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "未能搜索到输入地址信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}