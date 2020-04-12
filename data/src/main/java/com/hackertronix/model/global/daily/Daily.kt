package com.hackertronix.model.global.daily


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class Daily(
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

    @Embedded(prefix = "confirmed")
    @Json(name = "confirmed")
    val confirmed: DailyConfirmed = DailyConfirmed(),

    @Embedded(prefix = "deaths")
    @Json(name = "deaths")
    val deaths: DailyDeaths = DailyDeaths(),


    @Embedded(prefix = "recovered")
    @Json(name = "recovered")
    val recovered: DailyRecovered = DailyRecovered(),


    @Json(name = "active")
    val active: Int = 0,
    @Json(name = "deltaRecovered")
    val deltaRecovered: Float = 0f,
    @Json(name = "incidentRate")
    val incidentRate: Float = 0f,
    @Json(name = "peopleTested")
    val peopleTested: Int = 0,

    @PrimaryKey(autoGenerate = false)
    @Json(name = "reportDate")
    val reportDate: String = ""
)