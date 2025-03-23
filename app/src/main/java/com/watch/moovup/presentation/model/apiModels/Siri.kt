package com.watch.moovup.presentation.model.apiModels

import com.google.gson.annotations.SerializedName

data class Siri(
    @SerializedName("ServiceDelivery") val serviceDelivery: ServiceDelivery?
)