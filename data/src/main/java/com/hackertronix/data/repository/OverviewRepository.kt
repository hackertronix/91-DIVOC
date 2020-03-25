package com.hackertronix.data.repository

import com.hackertronix.OverviewRequestState
import com.hackertronix.OverviewRequestState.Failure
import com.hackertronix.OverviewRequestState.Loading
import com.hackertronix.OverviewRequestState.Success
import com.hackertronix.OverviewRequestState.SuccessWithoutResult
import com.hackertronix.data.local.Covid19StatsDatabase
import com.hackertronix.data.network.API
import com.hackertronix.model.global.daily.DailyStats
import com.hackertronix.model.global.overview.Overview
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class OverviewRepository(
    private val apiClient: API,
    private val database: Covid19StatsDatabase
) {

    val overviewEmitter = PublishRelay.create<OverviewRequestState>()
    val dailyStatsEmitter = PublishRelay.create<List<DailyStats>>()
    private val disposables = CompositeDisposable()

    fun getOverview() {
        overviewEmitter.accept(Loading)
        disposables += getOverviewFromDb()
            .flatMap {
                if (it.isEmpty()) {
                    return@flatMap getOverviewFromApi()
                }
                return@flatMap Flowable.just(Success(it.first()))
            }
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { requestState ->
                    overviewEmitter.accept(requestState)
                },
                onError = {
                    overviewEmitter.accept(Failure(it.message!!))
                }
            )
    }

    private fun getOverviewFromDb(): Flowable<List<Overview>> {
        return database.overviewDao().getOverview()
            .subscribeOn(Schedulers.io())
    }

    private fun getOverviewFromApi(): Flowable<OverviewRequestState> {
        return apiClient.getOverview()
            .map<OverviewRequestState> {
                Success(it)
            }
            .subscribeOn(Schedulers.io())
            .doOnSuccess { requestState ->
                when (requestState) {
                    is Success -> saveToDisk(requestState.overview)
                }
            }
            .onErrorReturn {
                OverviewRequestState.Failure(it.message!!)
            }.toFlowable()
    }

    private fun saveToDisk(overview: Overview) {
        database.overviewDao().insertOverview(overview)
    }

    fun dispose() {
        disposables.dispose()
    }

    fun refreshOverview() {
        overviewEmitter.accept(Loading)
        disposables += getOverviewFromApi()
            .subscribeBy(
                onComplete = {
                    overviewEmitter.accept(SuccessWithoutResult)
                },
                onError = {
                    overviewEmitter.accept(Failure(it.message!!))
                })
    }



    fun getDailyStats() {
        disposables += getDailyStatsFromDb()
            .flatMap {
                if (it.isEmpty()) {
                    return@flatMap getDailyStatsFromApi()
                }
                return@flatMap Flowable.just(it)
            }
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { response ->
                    dailyStatsEmitter.accept(response.reversed())
                },
                onError = {

                }
            )
    }
    private fun getDailyStatsFromDb(): Flowable<List<DailyStats>> {
        return database.dailyStatsDao().getDailyStats()
            .subscribeOn(Schedulers.io())
    }
    private fun getDailyStatsFromApi(): Flowable<List<DailyStats>> {
        return apiClient.getHistoricStats()
            .subscribeOn(Schedulers.io())
            .doOnSuccess { response ->
               saveToDisk(response)
            }
            .onErrorReturn {
               emptyList()
            }.toFlowable()
    }
    private fun saveToDisk(dailyStats: List<DailyStats>) {
        database.dailyStatsDao().insertDailyStat(dailyStats)
    }

    fun refreshDailyStats() {
        overviewEmitter.accept(Loading)
        disposables += getOverviewFromApi()
            .subscribeBy(
                onComplete = {
                    overviewEmitter.accept(SuccessWithoutResult)
                },
                onError = {
                    overviewEmitter.accept(Failure(it.message!!))
                })
    }
}
