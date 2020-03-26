package com.hackertronix.model.countries


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class Timelines(
    @PrimaryKey(autoGenerate = false)
    val id: Int =0,

    @Embedded(prefix = "confirmed")
    @Json(name = "confirmed")
    val confirmed: TimelineConfirmed = TimelineConfirmed(),

    @Embedded(prefix = "deaths")
    @Json(name = "deaths")
    val deaths: TimelineDeaths = TimelineDeaths(),


    @Embedded(prefix = "deaths")
    @Json(name = "recovered")
    val recovered: TimelineRecovered = TimelineRecovered()
)