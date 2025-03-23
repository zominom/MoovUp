package com.watch.moovup.presentation.model.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agencies")
data class Agency(
    @PrimaryKey val agencyId: Int,
    val agencyName: String,
    val agencyUrl: String,
    val agencyTimezone: String,
    val agencyLang: String,
    val agencyPhone: String?,
    val agencyFareUrl: String?
)