package com.test.covid19test.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country (val countryregion: String,
                    val confirmed: Int,
                    val deaths: Int,
                    val recovered: Int,
                    val countrycode: Countrycode) : Parcelable

@Parcelize
data class Countrycode (val iso2: String,
                        val iso3: String) : Parcelable
