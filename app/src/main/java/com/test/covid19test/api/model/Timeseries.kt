package com.test.covid19test.api.model

import java.util.*
import kotlin.collections.HashMap

data class Timeseries (val timeseries: HashMap<String, TimeseriesItem>)

data class TimeseriesItem (var time : Date? = null,
                           val confirmed: Int,
                           val deaths: Int,
                           val recovered: Int)