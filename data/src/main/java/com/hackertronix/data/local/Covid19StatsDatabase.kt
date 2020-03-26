package com.hackertronix.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hackertronix.data.local.converters.LocationsListConverter
import com.hackertronix.data.local.converters.RegionalListConverter
import com.hackertronix.data.local.converters.TimelinesConverter
import com.hackertronix.data.local.dao.global.countries.CountriesDao
import com.hackertronix.data.local.dao.global.overview.DailyStatsDao
import com.hackertronix.data.local.dao.global.overview.OverviewDao
import com.hackertronix.data.local.dao.india.LatestDao
import com.hackertronix.model.countries.CountriesStats
import com.hackertronix.model.countries.Latest
import com.hackertronix.model.countries.Location
import com.hackertronix.model.countries.Timeline
import com.hackertronix.model.countries.TimelineConfirmed
import com.hackertronix.model.countries.TimelineDeaths
import com.hackertronix.model.countries.TimelineRecovered
import com.hackertronix.model.countries.Timelines
import com.hackertronix.model.global.daily.Daily
import com.hackertronix.model.india.latest.Data
import com.hackertronix.model.india.latest.LatestIndianStats
import com.hackertronix.model.india.latest.Regional
import com.hackertronix.model.india.latest.Summary
import com.hackertronix.model.global.overview.Confirmed
import com.hackertronix.model.global.overview.Deaths
import com.hackertronix.model.global.overview.Overview
import com.hackertronix.model.global.overview.Recovered

@Database(
    entities = arrayOf(
        Confirmed::class,
        Deaths::class,
        Recovered::class,
        Overview::class,
        Data::class,
        LatestIndianStats::class,
        Regional::class,
        Summary::class,
        Daily::class,
        com.hackertronix.model.global.daily.DailyDeaths::class,
        com.hackertronix.model.global.daily.DailyRecovered::class,
        CountriesStats::class,
        Latest::class,
        Location::class,
        Timeline::class,
        TimelineConfirmed::class,
        TimelineDeaths::class,
        TimelineRecovered::class,
        Timelines::class
    ),
    version = 1
)
@TypeConverters(RegionalListConverter::class,LocationsListConverter::class,TimelinesConverter::class)
abstract class Covid19StatsDatabase : RoomDatabase() {

    abstract fun latestStatsDao(): LatestDao
    abstract fun overviewDao(): OverviewDao
    abstract fun dailyStatsDao(): DailyStatsDao
    abstract fun countriesStatsDao(): CountriesDao

    companion object {
        private val sLock = Any()

        private var INSTANCE: Covid19StatsDatabase? = null

        fun getDatabase(context: Context): Covid19StatsDatabase {
            if (INSTANCE == null) {
                synchronized(sLock) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                                context.applicationContext,
                                Covid19StatsDatabase::class.java, "covid19stats.db"
                            )
                            .build()
                    }
                    return INSTANCE!!
                }
            }
            return INSTANCE!!
        }
    }
}