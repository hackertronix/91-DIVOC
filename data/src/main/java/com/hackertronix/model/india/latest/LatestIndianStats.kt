package com.hackertronix.model.india.latest


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hackertronix.model.india.latest.Data
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class LatestIndianStats(

    @Embedded(prefix = "data")
    @Json(name = "data")
    val data: Data = Data(),

    @PrimaryKey(autoGenerate = false)
    @Json(name = "lastRefreshed")
    val lastRefreshed: String = ""
)