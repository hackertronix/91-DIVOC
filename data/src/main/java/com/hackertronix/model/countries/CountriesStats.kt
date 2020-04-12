package com.hackertronix.model.countries

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountriesStats(
    @Json(name = "locations")
    val locations: List<Location> = listOf()
)