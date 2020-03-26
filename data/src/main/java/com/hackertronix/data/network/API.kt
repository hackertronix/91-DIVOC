package com.hackertronix.data.network

import com.hackertronix.data.network.converters.LocationArrayListAdapter
import com.hackertronix.data.network.converters.TimelineMapAdapter
import com.hackertronix.model.countries.CountriesStats
import com.hackertronix.model.global.daily.Daily
import com.hackertronix.model.global.overview.Overview
import com.hackertronix.model.india.latest.LatestIndianStats
import com.squareup.moshi.Moshi
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface API {

    @GET("/api")
    fun getOverview(): Single<Overview>

    @GET("/api/daily")
    fun getHistoricStats(): Single<List<Daily>>
}

interface IndApi {
    @GET("covid19-in/stats/latest")
    fun getLatestStats(): Single<LatestIndianStats>
}

interface TimelinesApi {
    @GET("/v2/locations?timelines=1")
    fun getTimelinesForAllCountries(): Single<CountriesStats>
}

fun provideIndianApiClient(): IndApi {
    val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.create()
        )
        .addConverterFactory(
            MoshiConverterFactory.create()
        )
        .baseUrl("https://api.rootnet.in/")
        .build()

    return retrofit.create(IndApi::class.java)
}

fun provideApiClient(): API {
    val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.create()
        )
        .addConverterFactory(
            MoshiConverterFactory.create()
        )
        .baseUrl("https://covid19.mathdro.id")
        .build()

    return retrofit.create(API::class.java)
}

fun provideTimelinesApiClient(): TimelinesApi {
    val moshi: Moshi = Moshi.Builder()
        .add(TimelineMapAdapter())
        .build()

    val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.create()
        )
        .addConverterFactory(
            MoshiConverterFactory.create(moshi)
        )
        .baseUrl("https://coronavirus-tracker-api.herokuapp.com/")
        .build()

    return retrofit.create(TimelinesApi::class.java)
}
