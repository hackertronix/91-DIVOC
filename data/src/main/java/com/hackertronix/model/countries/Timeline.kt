package com.hackertronix.model.countries


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
class Timeline(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)