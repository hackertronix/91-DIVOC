package com.hackertronix.data.repository

import com.hackertronix.data.local.Covid19StatsDatabase
import com.hackertronix.data.network.API
import com.hackertronix.model.overview.Overview
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OverviewRepository(
    private val apiClient: API,
    private val database: Covid19StatsDatabase
) {

    fun getOverview(): Observable<Overview> {
        return database.overviewDao().getOverview()
            .toObservable()
            .flatMap {
                if (it.isEmpty()) {
                    return@flatMap getOverviewFromApi()
                }
                return@flatMap Observable.just(it.first())
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getOverviewFromApi(): Observable<Overview> {
        return apiClient.getOverview().toObservable()
            .map {
                database.overviewDao().deleteOverview()
                database.overviewDao().insertOverview(it)
                return@map it
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}