package com.hackertronix.data.local.dao.india

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hackertronix.model.india.latest.Latest
import io.reactivex.Flowable

@Dao
interface LatestDao {

    @Insert
    fun insertLatest(vararg: Latest)

    @Delete
    fun delete(latestData: Latest)

    @Query("DELETE FROM Latest")
    fun deleteLatest()

    @Query("SELECT * FROM LATEST ORDER BY lastRefreshed DESC LIMIT 1")
    fun getLatestStats(): Flowable<List<Latest>>
}