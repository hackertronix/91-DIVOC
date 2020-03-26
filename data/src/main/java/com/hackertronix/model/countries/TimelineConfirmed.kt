package com.hackertronix.model.countries

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class TimelineConfirmed(
    @PrimaryKey(autoGenerate = true)
    val timelineConfirmedId: Int = 0,

    @Json(name = "latest")
    val timelineConfirmedLatest: Int = 0,

    @Json(name = "timeline")
    val timelineConfirmed: Map<String, Int> = mapOf()
)