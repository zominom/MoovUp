package com.watch.moovup.presentation.model.apiModels

import com.google.gson.annotations.SerializedName

data class MonitoredStopVisit(
    @SerializedName("RecordedAtTime") val recordedAtTime: String?,
    @SerializedName("ItemIdentifier") val itemIdentifier: String?,
    @SerializedName("MonitoringRef") val monitoringRef: String?,
    @SerializedName("MonitoredVehicleJourney") val monitoredVehicleJourney: MonitoredVehicleJourney?
)