package com.hackertronix.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hackertronix.data.local.converters.RegionalListConverter
import com.hackertronix.data.local.dao.global.DailyStatsDao
import com.hackertronix.data.local.dao.global.OverviewDao
import com.hackertronix.data.local.dao.india.LatestDao
import com.hackertronix.model.global.daily.DailyStats
import com.hackertronix.model.india.latest.Data
import com.hackertronix.model.india.latest.Latest
import com.hackertronix.model.india.latest.Regional
import com.hackertronix.model.india.latest.Summary
import com.hackertronix.model.global.overview.Confirmed
import com.hackertronix.model.global.overview.Countries
import com.hackertronix.model.global.overview.CountriesResponse
import com.hackertronix.model.global.overview.Deaths
import com.hackertronix.model.global.overview.Overview
import com.hackertronix.model.global.overview.Recovered

@Database(
    entities = arrayOf(
        Confirmed::class,
        Deaths::class,
        Recovered::class,
        Overview::class,
        Countries::class,
        CountriesResponse::class,
        Data::class,
        Latest::class,
        Regional::class,
        Summary::class,
        DailyStats::class
    ),
    version = 1
)
@TypeConverters(RegionalListConverter::class)
abstract class Covid19StatsDatabase : RoomDatabase() {

    abstract fun latestStatsDao(): LatestDao
    abstract fun overviewDao(): OverviewDao
    abstract fun dailyStatsDao(): DailyStatsDao

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