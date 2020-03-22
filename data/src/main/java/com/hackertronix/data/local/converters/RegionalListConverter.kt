package com.hackertronix.data.local.converters

import androidx.room.TypeConverter
import com.hackertronix.model.india.latest.Regional
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class RegionalListConverter {

    @TypeConverter
    fun fromList(listOfRegionalObjects: List<Regional>): String {
        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            Regional::class.java
        )
        val adapter: JsonAdapter<List<Regional>> = moshi.adapter(listMyData)
        return adapter.toJson(listOfRegionalObjects)
    }
    @TypeConverter
    fun toList(listOfRegionalObjectsAsString: String): List<Regional> {
        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            Regional::class.java
        )
        val adapter: JsonAdapter<List<Regional>> = moshi.adapter(listMyData)
        return adapter.fromJson(listOfRegionalObjectsAsString)!!
    }
}
