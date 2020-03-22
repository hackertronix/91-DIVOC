package com.hackertronix.model.india.latest


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Data(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @Embedded(prefix = "summary")
    @Json(name = "summary")
    val summary: Summary = Summary(),

    @Json(name = "regional")
    val regional: List<Regional> = listOf()
)