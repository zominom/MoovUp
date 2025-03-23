package com.watch.moovup.presentation.model.apiModels

import com.google.gson.annotations.SerializedName

data class ServiceDelivery(
    @SerializedName("ResponseTimestamp") val responseTimestamp: String?,
    @SerializedName("ProducerRef") val producerRef: String?,
    @SerializedName("ResponseMessageIdentifier") val responseMessageIdentifier: String?,
    @SerializedName("RequestMessageRef") val requestMessageRef: String?,
    @SerializedName("Status") val status: String?,
    @SerializedName("StopMonitoringDelivery") val stopMonitoringDelivery: List<StopMonitoringDelivery>?
)