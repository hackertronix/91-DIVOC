package com.hackertronix.data.local.dao.global.countries

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackertronix.model.countries.CountriesStats
import io.reactivex.Flowable

@Dao
interface CountriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountryStats(vararg: CountriesStats)

    @Delete
    fun delete(countries: CountriesStats)

    @Query("DELETE FROM CountriesStats")
    fun deleteAll()

    @Query("SELECT * FROM CountriesStats")
    fun getCountriesStats(): Flowable<List<CountriesStats>>
}