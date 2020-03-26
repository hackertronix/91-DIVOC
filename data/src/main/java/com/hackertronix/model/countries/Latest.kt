package com.hackertronix.model.countries

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class Latest(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Json(name = "confirmed")
    val confirmed: Int = 0,
    @Json(name = "deaths")
    val deaths: Int = 0,
    @Json(name = "recovered")
    val recovered: Int = 0
)