package com.hackertronix.data.network.converters

import com.hackertronix.model.countries.Location
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class LocationArrayListAdapter {

    @ToJson
    fun arrayListToJson(list: ArrayList<Location>): List<Location> = list

    @FromJson
    fun arrayListFromJson(list: List<Location>): ArrayList<Location> = ArrayList(list)
}