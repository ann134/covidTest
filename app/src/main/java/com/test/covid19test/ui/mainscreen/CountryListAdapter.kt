package com.test.covid19test.ui.mainscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.covid19test.R
import com.test.covid19test.api.model.Country
import com.test.covid19test.util.PicassoUtil
import kotlinx.android.synthetic.main.item_country.view.*

class CountryListAdapter (private val clickListener: (Country) -> Unit) :
    RecyclerView.Adapter<CountryListAdapter.CountryHolder>() {

    var list : ArrayList<Country> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return CountryHolder(v)
    }

    override fun onBindViewHolder(holder: CountryHolder, position: Int) {
        val item = list[position]
        holder.bindItem(item, clickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CountryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(item: Country, clickListener: (Country) -> Unit) {
            itemView.name.text = item.countryregion
            PicassoUtil.getPicasso().load("https://www.freeflagicons.com/download/?texture=1&country="+ item.countryregion.toLowerCase()).into(itemView.image)
            itemView.setOnClickListener { clickListener(item) }
        }

    }
}