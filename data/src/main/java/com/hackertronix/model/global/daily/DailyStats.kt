package com.hackertronix.model.global.daily


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class DailyStats(
    @Json(name = "totalConfirmed")
    val totalConfirmed: Int = 0,

    @Json(name = "mainlandChina")
    val mainlandChina: Int = 0,

    @Json(name = "otherLocations")
    val otherLocations: Int = 0,

    @Json(name = "deltaConfirmed")
    val deltaConfirmed: Int = 0,

    @Json(name = "totalRecovered")
    val totalRecovered: Int = 0,

    @Json(name = "active")
    val active: Int = 0,

    @Json(name = "deltaRecovered")
    val deltaRecovered: Int = 0,

    @Json(name = "incidentRate")
    val incidentRate: Int = 0,

    @Json(name = "peopleTested")
    val peopleTested: Int = 0,

    @PrimaryKey(autoGenerate = false)
    @Json(name = "reportDate")
    val reportDate: String = ""
)