package com.watch.moovup.presentation.model.apiModels

import com.google.gson.annotations.SerializedName

data class StopMonitoringDelivery(
    @SerializedName("-version") val version: String?,
    @SerializedName("ResponseTimestamp") val responseTimestamp: String?,
    @SerializedName("Status") val status: String?,
    @SerializedName("MonitoredStopVisit") val monitoredStopVisit: List<MonitoredStopVisit>?
)