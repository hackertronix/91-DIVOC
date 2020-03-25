package com.hackertronix.model.global.overview

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Confirmed(
    @PrimaryKey(autoGenerate = true)
    val confirmedId: Int = 0,
    @field:Json(name = "value")
    val confirmedCasesCount: Int = 0,
    @field:Json(name = "detail")
    val confirmedCasesDetail: String = ""
)