package com.hackertronix.data.local.dao.global

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackertronix.model.global.overview.Overview
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface OverviewDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOverview(vararg: Overview)

    @Query("DELETE FROM Overview")
    fun deleteOverview()

    @Query("SELECT * FROM Overview LIMIT 1")
    fun getOverview(): Flowable<List<Overview>>
}