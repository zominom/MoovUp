package com.watch.moovup.presentation.model.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "trips",
    foreignKeys = [
        ForeignKey(entity = Route::class, parentColumns = ["routeId"], childColumns = ["routeId"])
    ])
data class Trip(
    @PrimaryKey val tripId: String,
    val routeId: Int,
    val serviceId: Int,
    val tripHeadsign: String,
    val directionId: Int,
    val shapeId: Int,
    val wheelchairAccessible: Int
)