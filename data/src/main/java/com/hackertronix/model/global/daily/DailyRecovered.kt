package com.hackertronix.model.global.daily


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class DailyRecovered(
    @PrimaryKey
    val id: Int =0,
    @Json(name = "total")
    val total: Int = 0,
    @Json(name = "china")
    val china: Int = 0,
    @Json(name = "outsideChina")
    val outsideChina: Int = 0
)