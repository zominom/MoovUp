package com.watch.moovup.presentation.model.apiModels

import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("Siri") val siri: Siri?
)