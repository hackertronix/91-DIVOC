package com.hackertronix.data.local.dao.global

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackertronix.model.global.daily.DailyStats
import io.reactivex.Flowable

@Dao
interface DailyStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDailyStat(vararg: List<DailyStats>)

    @Delete
    fun delete(dailyStats: DailyStats)

    @Query("DELETE FROM DailyStats")
    fun deleteLatest()

    @Query("SELECT * FROM DailyStats GROUP BY reportDate ORDER BY reportDate DESC LIMIT 60")
    fun getDailyStats(): Flowable<List<DailyStats>>
}