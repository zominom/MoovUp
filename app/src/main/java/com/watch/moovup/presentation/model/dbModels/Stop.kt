package com.watch.moovup.presentation.model.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stops")
data class Stop(
    @PrimaryKey val stopId: Int,
    val stopCode: Int,
    val stopName: String,
    val stopDesc: String,
    val stopLat: Double,
    val stopLon: Double,
    val locationType: Int,
    val parentStation: String?,
    val zoneId: Int
)