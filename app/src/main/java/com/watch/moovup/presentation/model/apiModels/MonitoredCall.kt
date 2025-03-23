package com.watch.moovup.presentation.model.apiModels

import com.google.gson.annotations.SerializedName

data class MonitoredCall(
    @SerializedName("StopPointRef") val stopPointRef: String?,
    @SerializedName("Order") val order: String?,
    @SerializedName("AimedArrivalTime") val aimedArrivalTime: String?,
    @SerializedName("ExpectedArrivalTime") val expectedArrivalTime: String?,
    @SerializedName("DistanceFromStop") val distancefromstop: String?
)