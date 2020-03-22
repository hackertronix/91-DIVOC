package com.hackertronix.model.overview

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Overview(
    @Embedded
    @field:Json(name = "confirmed")
    val confirmed: Confirmed = Confirmed(),

    @Embedded
    @field:Json(name = "recovered")
    val recovered: Recovered = Recovered(),

    @Embedded
    @field:Json(name = "deaths")
    val deaths: Deaths = Deaths(),

    @PrimaryKey
    @field:Json(name = "lastUpdate")
    val lastUpdate: String = ""
)