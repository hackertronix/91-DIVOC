package com.hackertronix.model.global.overview

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Deaths(
    @PrimaryKey(autoGenerate = true)
    val deathId: Int = 0,
    @field:Json(name = "value")
    val deathCasesCount: Int = 0,
    @field:Json(name = "detail")
    val deathCasesDetail: String = ""
)