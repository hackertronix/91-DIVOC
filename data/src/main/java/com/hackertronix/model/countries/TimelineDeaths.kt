package com.hackertronix.model.countries


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.json.JSONObject

@JsonClass(generateAdapter = true)
@Entity
data class TimelineDeaths(
    @PrimaryKey(autoGenerate = true)
    val timelineDeathsId: Int = 0,

    @Json(name = "latest")
    val timelineDeathsLatest: Int = 0,

    @Json(name = "timeline")
    val timelineDeaths: Map<String, Int> = mapOf())