package com.test.covid19test.api

import com.google.gson.GsonBuilder
import com.test.covid19test.api.model.Brief
import com.test.covid19test.api.model.Country
import com.test.covid19test.api.model.Timeseries
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface CovidApiService {

    @GET("jhu-edu/brief")
    fun getBrief(): Observable<Brief>

    @GET("jhu-edu/latest")
    fun getLatestByCountries(@Query("onlyCountries") onlyCountries: Boolean): Observable<ArrayList<Country>>

    @GET("jhu-edu/timeseries")
    fun getTimeseriesByCountry(@Query("iso2") iso2: String, @Query("onlyCountries") onlyCountries: Boolean): Observable<ArrayList<Timeseries>>

    companion object Factory {
        fun create(): CovidApiService {

            val gson = GsonBuilder()
                .setDateFormat("dd/MM/yy")
                .create()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/")
                .build()

            return retrofit.create(CovidApiService::class.java)
        }
    }
}