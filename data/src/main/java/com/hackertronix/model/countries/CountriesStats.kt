package com.hackertronix.model.countries

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class CountriesStats(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,


    @Json(name = "locations")
    val locations: List<Location> = listOf()
)