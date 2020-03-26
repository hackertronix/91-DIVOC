package com.hackertronix.model.countries

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class TimelineRecovered(
    @PrimaryKey(autoGenerate = true)
    val timelineRecoveredId: Int = 0,

    @Json(name = "latest")
    val timelineRecoveredLatest: Int = 0,

    @Json(name = "timeline")
    val timelineRecovered: Map<String, Int> = mapOf())