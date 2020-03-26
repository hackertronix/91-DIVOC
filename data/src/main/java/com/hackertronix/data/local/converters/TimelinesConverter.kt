package com.hackertronix.data.local.converters

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class TimelinesConverter {
    @TypeConverter
    fun fromMap(listOfLocationObjects: Map<String, Int>): String {
        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableMap::class.java,
            String::class.java,
            Int::class.java
        )
        val adapter: JsonAdapter<Map<String, Int>> = moshi.adapter(listMyData)
        return adapter.toJson(listOfLocationObjects)
    }

    @TypeConverter
    fun toMap(listOfLocationObjectsAsString: String): Map<String, Int> {
        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableMap::class.java,
            String::class.java,
            Int::class.java
        )
        val adapter: JsonAdapter<Map<String, Int>> = moshi.adapter(listMyData)
        return adapter.fromJson(listOfLocationObjectsAsString)!!
    }
}