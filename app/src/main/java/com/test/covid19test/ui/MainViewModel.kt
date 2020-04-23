package com.test.covid19test.ui

import androidx.lifecycle.ViewModel
import com.test.covid19test.repo.CovidRepository
import com.test.covid19test.api.model.Brief
import com.test.covid19test.api.model.Country
import com.test.covid19test.api.model.Timeseries
import com.test.covid19test.api.model.TimeseriesItem
import io.reactivex.Observable

class MainViewModel : ViewModel(){

    fun getBrief(): Observable<Brief> {
        return CovidRepository.getBrief()
    }

    fun getLatestByCountries(): Observable<ArrayList<Country>> {
        return CovidRepository.getLatestByCountries()
    }

    fun getLatestByCountries(query: String): Observable<ArrayList<Country>> {
        return CovidRepository.getLatestByCountries(query)
    }

    fun getTimeseriesByCountry(iso2: String): Observable<ArrayList<TimeseriesItem>> {
        return CovidRepository.getTimeseriesByCountry(iso2)
    }
}