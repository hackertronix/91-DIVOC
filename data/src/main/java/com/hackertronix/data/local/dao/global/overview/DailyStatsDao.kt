package com.hackertronix.data.local.dao.global.overview

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackertronix.model.global.daily.Daily
import io.reactivex.Flowable

@Dao
interface DailyStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDailyStat(vararg: List<Daily>)

    @Delete
    fun delete(dailyStats: Daily)

    @Query("DELETE FROM Daily")
    fun deleteLatest()

    @Query("SELECT * FROM Daily ORDER BY reportDate DESC ")
    fun getDailyStats(): Flowable<List<Daily>>
}