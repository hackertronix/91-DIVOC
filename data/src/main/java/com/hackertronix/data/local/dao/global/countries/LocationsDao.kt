package com.hackertronix.data.local.dao.global.countries

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackertronix.model.countries.CountriesStats
import com.hackertronix.model.countries.Location
import io.reactivex.Flowable

@Dao
interface LocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocations(vararg: List<Location>)

    @Delete
    fun delete(countries: Location)

    @Query("DELETE FROM Location")
    fun deleteAll()

    @Query("SELECT * FROM Location")
    fun getAllCountriesStats(): Flowable<List<Location>>

    @Query("SELECT * FROM Location where countryCode = :countryCode")
    fun getCountryStatsForCountryCode(countryCode: String): Flowable<List<Location>>
}