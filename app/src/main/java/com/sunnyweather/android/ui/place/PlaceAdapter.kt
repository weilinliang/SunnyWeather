package com.sunnyweather.android.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: Fragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        var viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            var position = viewHolder.adapterPosition
            var place = placeList[position]
            WeatherActivity.startAction(
                parent.context,
                place.location.lng,
                place.location.lat,
                place.name
            )
//            fragment.activity?.finish()
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = placeList[position]
        holder.placeName.text = item.name
        holder.placeAddress.text = item.address
    }

    override fun getItemCount(): Int {
        return placeList.size
    }
}