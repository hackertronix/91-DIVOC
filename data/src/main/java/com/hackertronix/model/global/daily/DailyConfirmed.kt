package com.hackertronix.model.global.daily


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyConfirmed(
    @Json(name = "total")
    val total: Int = 0,
    @Json(name = "china")
    val china: Int = 0,
    @Json(name = "outsideChina")
    val outsideChina: Int = 0
)