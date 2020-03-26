package com.hackertronix.data.local.converters

import androidx.room.TypeConverter
import com.hackertronix.model.countries.Location
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class LocationsListConverter {
    @TypeConverter
    fun fromList(listOfLocationObjects: List<Location>): String {
        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            Location::class.java
        )
        val adapter: JsonAdapter<List<Location>> = moshi.adapter(listMyData)
        return adapter.toJson(listOfLocationObjects)
    }
    @TypeConverter
    fun toList(listOfLocationObjectsAsString: String): List<Location> {
        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            Location::class.java
        )
        val adapter: JsonAdapter<List<Location>> = moshi.adapter(listMyData)
        return adapter.fromJson(listOfLocationObjectsAsString)!!
    }
}