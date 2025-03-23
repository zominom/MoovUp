package com.watch.moovup.presentation.model.apiModels

import com.google.gson.annotations.SerializedName

data class FramedVehicleJourneyRef(
    @SerializedName("DataFrameRef") val dataFrameRef: String?,
    @SerializedName("DatedVehicleJourneyRef") val datedVehicleJourneyRef: String?
)