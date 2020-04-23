package com.test.covid19test.ui.countryscreen

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.test.covid19test.R
import com.test.covid19test.api.model.Country
import com.test.covid19test.api.model.TimeseriesItem
import com.test.covid19test.ui.MainViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_country.*


class CountryFragment : Fragment(R.layout.fragment_country) {

    var country: Country? = null
    private lateinit var viewModel: MainViewModel
    private var briefDisposable = CompositeDisposable()
    lateinit var adapter: TimeseriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        country = arguments?.getParcelable("country") as Country?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TimeseriesAdapter()
        recycler.adapter = adapter

        tv_title.text = country?.countryregion
        tv_confirmed_value.text = country?.confirmed.toString()
        tv_recovered_value.text = country?.recovered.toString()
        tv_deaths_value.text = country?.deaths.toString()
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    override fun onStop() {
        super.onStop()
        briefDisposable.clear()
    }

    private fun loadData() {
        country?.countrycode?.iso2?.let {
            briefDisposable.add(

                viewModel.getTimeseriesByCountry(it)

                    .subscribe({ result ->
                        showList(result)
                        showChart(result)

                    }, { error ->
                        error.printStackTrace()
                    })
            )
        }
    }

    private fun showList(list: ArrayList<TimeseriesItem>) {
        adapter.list = list
    }


    private fun showChart(list: ArrayList<TimeseriesItem>) {
        var reverseList = ArrayList<TimeseriesItem>(list)
        reverseList.reverse()
        chart.visibility = View.VISIBLE

        chart.xAxis.disableAxisLineDashedLine()
        chart.xAxis.isEnabled = false

        //legend
        val l = chart.legend
        l.formSize = 13f
        l.textSize = 11f
        l.textColor = Color.BLACK
        l.formToTextSpace = 5f
        l.xEntrySpace = 10f
        //property
        chart.setTouchEnabled(false)
        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
        chart.setClipValuesToContent(false)

        var confirmedList = ArrayList<Entry>()
        var deathList = ArrayList<Entry>()
        var recoveredList = ArrayList<Entry>()
        for (i in 0..reverseList.size - 1) {
            confirmedList.add(Entry(i.toFloat(), reverseList[i].confirmed.toFloat()))
            deathList.add(Entry(i.toFloat(), reverseList[i].deaths.toFloat()))
            recoveredList.add(Entry(i.toFloat(), reverseList[i].recovered.toFloat()))
        }

        val dataSet1 = LineDataSet(confirmedList, "Заразившиеся")
        dataSet1.color = Color.RED
        dataSet1.valueTextColor = Color.RED
        dataSet1.setDrawValues(false)
        dataSet1.setDrawCircles(false)


        val dataSet2 = LineDataSet(deathList, "Смерти")
        dataSet2.color = Color.BLACK
        dataSet2.valueTextColor = Color.BLACK
        dataSet2.setDrawValues(false)
        dataSet2.setDrawCircles(false)

        val dataSet3 = LineDataSet(recoveredList, "Выздоровевшие")
        dataSet3.color = Color.GREEN
        dataSet3.valueTextColor = Color.GREEN
        dataSet3.setDrawValues(false)
        dataSet3.setDrawCircles(false)


        // Setting Data
        val data = LineData(dataSet1, dataSet2, dataSet3)
        chart.data = data
        chart.animateX(3000)
        //refresh
        chart.invalidate()
    }
}