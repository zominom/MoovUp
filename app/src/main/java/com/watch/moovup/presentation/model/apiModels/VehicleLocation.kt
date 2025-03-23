package com.watch.moovup.presentation.model.apiModels

import com.google.gson.annotations.SerializedName

data class VehicleLocation(
    @SerializedName("Longitude") val longitude: String?,
    @SerializedName("Latitude") val latitude: String?
)