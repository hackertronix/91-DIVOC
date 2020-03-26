package com.hackertronix.data.network.converters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class TimelineMapAdapter {

    @ToJson
    fun mapToJson(timeLine: HashMap<String, Int>): Map<String, Int> = timeLine

    @FromJson
    fun mapFromJson(timeLine: Map<String, Int>): HashMap<String, Int> = HashMap(timeLine)
}