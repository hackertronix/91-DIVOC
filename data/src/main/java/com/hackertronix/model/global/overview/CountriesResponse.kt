package com.hackertronix.model.global.overview

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hackertronix.model.global.overview.Countries
import com.squareup.moshi.Json
@Entity
data class CountriesResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @Embedded(prefix = "cntry")
    @field:Json(name = "countries")
    val countries: Countries = Countries()
)