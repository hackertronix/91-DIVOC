package com.hackertronix.data.local.converters

import androidx.room.TypeConverter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): String? {
        return if (value != null)
            DateTimeFormat.forPattern("dd MMM yyyy, HH:MM a")
                .print(
                    DateTime(value)
                        .toLocalDateTime()
                        .toDateTime().millis
                )
        else null
    }

    @TypeConverter
    fun toTimeStamp(lastUpdate: String?): Long? {
        return if (lastUpdate != null)
            DateTime(lastUpdate)
                .toLocalDateTime()
                .toDateTime().millis
        else null
    }
}