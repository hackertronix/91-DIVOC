package com.hackertronix.model.global.overview

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Recovered(
    @PrimaryKey(autoGenerate = true)
    val recoveredId: Int=0,
    @field:Json(name = "value")
    val recoveredCasesCount: Int = 0,
    @field:Json(name = "detail")
    val recoveredCasesDetail: String = ""
)