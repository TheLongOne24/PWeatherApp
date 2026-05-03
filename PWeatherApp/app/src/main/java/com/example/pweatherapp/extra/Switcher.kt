package com.example.pweatherapp.extra

import com.example.pweatherapp.R

    public fun Switcher(type: String?) : Int {
        val back = when(type) {
            "01d" -> R.drawable.one_day
            "02d" -> R.drawable.two_day
            "03d" -> R.drawable.three_day
            "04d" -> R.drawable.four_day
            "09d" -> R.drawable.five_day
            "10d" -> R.drawable.six_day
            "11d" -> R.drawable.seven_day
            "13d" -> R.drawable.eight_day
            "50d" -> R.drawable.nine_day
            "01n" -> R.drawable.one_night
            "02n" -> R.drawable.two_night
            "03n" -> R.drawable.three_night
            "04n" -> R.drawable.four_night
            "09n" -> R.drawable.five_night
            "10n" -> R.drawable.six_night
            "11n" -> R.drawable.seven_night
            "13n" -> R.drawable.eight_night
            "50n" -> R.drawable.nine_night
            else -> null
        }
        if (back != null) {
            return back
        }
        else return R.drawable.one_day
    }