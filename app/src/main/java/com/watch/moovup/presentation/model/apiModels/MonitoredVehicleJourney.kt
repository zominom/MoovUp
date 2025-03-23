package com.watch.moovup.presentation.model.apiModels

import com.google.gson.annotations.SerializedName

data class MonitoredVehicleJourney(
    @SerializedName("LineRef") val lineRef: String?,
    @SerializedName("DirectionRef") val directionRef: String?,
    @SerializedName("FramedVehicleJourneyRef") val framedVehicleJourneyRef: FramedVehicleJourneyRef?,
    @SerializedName("PublishedLineName") val publishedLineName: String?,
    @SerializedName("OperatorRef") val operatorRef: String?,
    @SerializedName("DestinationRef") val destinationRef: String?,
    @SerializedName("OriginAimedDepartureTime") val originAimedDepartureTime: String?,
    @SerializedName("ConfidenceLevel") val confidenceLevel: String?,
    @SerializedName("VehicleLocation") val vehicleLocation: VehicleLocation?,
    @SerializedName("Bearing") val bearing: String?,
    @SerializedName("Velocity") val velocity: String?,
    @SerializedName("VehicleRef") val vehicleRef: String?,
    @SerializedName("MonitoredCall") val monitoredCall: MonitoredCall?
)