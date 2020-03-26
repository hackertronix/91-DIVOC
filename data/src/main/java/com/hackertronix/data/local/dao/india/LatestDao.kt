package com.hackertronix.data.local.dao.india

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hackertronix.model.india.latest.LatestIndianStats
import io.reactivex.Flowable

@Dao
interface LatestDao {

    @Insert
    fun insertLatest(vararg: LatestIndianStats)

    @Delete
    fun delete(latestData: LatestIndianStats)

    @Query("DELETE FROM LatestIndianStats")
    fun deleteLatest()

    @Query("SELECT * FROM LatestIndianStats ORDER BY lastRefreshed DESC LIMIT 1")
    fun getLatestStats(): Flowable<List<LatestIndianStats>>
}