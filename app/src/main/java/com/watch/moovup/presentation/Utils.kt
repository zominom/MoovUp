package com.watch.moovup.presentation

import android.icu.text.DecimalFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.math.*
import kotlin.time.Duration

object Utils {
    fun calculateSquaredEuclideanDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        return (lat2 - lat1) * (lat2 - lat1) + (lon2 - lon1) * (lon2 - lon1)
    }

    fun approximateDistanceInMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
        val squaredDistance = calculateSquaredEuclideanDistance(lat1, lon1, lat2, lon2)
        val averageLatitude = (lat1 + lat2) / 2.0
        // 1 degree is approx. 111 km, correction for longitude
        return (sqrt(squaredDistance)* 111 * cos(averageLatitude * PI / 180) * 1000).roundToInt()
    }

    fun formatTime(timeString: String): String {
        if (timeString.isBlank() || !timeString.matches(Regex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+-]\\d{2}:\\d{2}"))) {
            return "Invalid Time"
        }

        val parts = timeString.split("T")
        val timePart = parts[1].split("+")[0].split(":")
        val hour = timePart[0]
        val minute = timePart[1]

        return "$hour:$minute"
    }

    fun getMinutesDifference(targetTimeString: String): Int? {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val targetTime: LocalTime = LocalTime.parse(targetTimeString, formatter)
        val currentTime: LocalTime = LocalTime.now()

        val targetMinutes = targetTime.toSecondOfDay() / 60
        val currentMinutes = currentTime.toSecondOfDay() / 60

        return  targetMinutes - currentMinutes
    }

}