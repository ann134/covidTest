package com.test.covid19test.repo

import com.test.covid19test.api.CovidApiService
import com.test.covid19test.api.model.Brief
import com.test.covid19test.api.model.Country
import com.test.covid19test.api.model.TimeseriesItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat

object CovidRepository {

    private var apiService = CovidApiService.create()

    fun getBrief(): Observable<Brief> {
        return apiService.getBrief()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLatestByCountries(): Observable<ArrayList<Country>> {
        return apiService.getLatestByCountries(true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLatestByCountries(name: String): Observable<ArrayList<Country>> {
        return apiService.getLatestByCountries(true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { list ->
                list.filter {
                    it.countryregion.toLowerCase().contains(name.toLowerCase())
                } as ArrayList<Country>?
            }
    }

    fun getTimeseriesByCountry(iso2: String): Observable<ArrayList<TimeseriesItem>> {
        return apiService.getTimeseriesByCountry(iso2, true)
            .flatMap { Observable.just(it[0].timeseries) }
            .flatMap { Observable.just(maptolist(it)) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

    private fun maptolist(map: HashMap<String, TimeseriesItem>): ArrayList<TimeseriesItem> {
        val list = ArrayList<TimeseriesItem>()
        for ((key, value) in map) {
            value.time = SimpleDateFormat("MM/dd/yy").parse(key)
            list.add(value)
        }
        list.sortWith(Comparator<TimeseriesItem> { p1, p2 ->
            when {
                p1.time!!.time < p2.time!!.time -> 1
                p1.time!!.time == p2.time!!.time -> 0
                else -> -1
            }
        })
        return list
    }
}