package com.test.covid19test.util

import com.squareup.picasso.Picasso

object PicassoUtil {

    fun getPicasso(): Picasso {
        return Picasso.get()
    }

}