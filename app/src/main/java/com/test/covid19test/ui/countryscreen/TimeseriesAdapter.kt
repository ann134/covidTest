package com.test.covid19test.ui.countryscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.covid19test.R
import com.test.covid19test.api.model.TimeseriesItem
import kotlinx.android.synthetic.main.item_timeseries.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimeseriesAdapter : RecyclerView.Adapter<TimeseriesAdapter.TimeseriesHolder>() {

    var list: ArrayList<TimeseriesItem> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeseriesHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_timeseries, parent, false)
        return TimeseriesHolder(v)
    }

    override fun onBindViewHolder(holder: TimeseriesHolder, position: Int) {
        val item = list[position]
        holder.bindItem(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class TimeseriesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(item: TimeseriesItem) {
            itemView.tv_title.text = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT).format(item.time)
            itemView.tv_confirmed_value.text = item.confirmed.toString()
            itemView.tv_recovered_value.text = item.recovered.toString()
            itemView.tv_deaths_value.text = item.deaths.toString()
        }

    }
}