package com.watch.moovup.presentation.model.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "routes",
    foreignKeys = [
        ForeignKey(entity = Agency::class, parentColumns = ["agencyId"], childColumns = ["agencyId"])
    ])
data class Route(
    @PrimaryKey val routeId: Int,
    val agencyId: Int,
    val routeShortName: String,
    val routeLongName: String,
    val routeDesc: String?,
    val routeType: Int,
    val routeColor: String?
)