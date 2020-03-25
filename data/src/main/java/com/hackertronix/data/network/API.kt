package com.hackertronix.data.network

import com.hackertronix.model.global.daily.DailyStats
import com.hackertronix.model.india.latest.Latest
import com.hackertronix.model.global.overview.Overview
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface API {

    @GET("/api")
    fun getOverview(): Single<Overview>

    @GET("/api/daily")
    fun getHistoricStats(): Single<List<DailyStats>>
}

interface IndApi{
    @GET("covid19-in/stats/latest")
    fun getLatestStats(): Single<Latest>
}

fun provideIndianApiClient(): IndApi{
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
