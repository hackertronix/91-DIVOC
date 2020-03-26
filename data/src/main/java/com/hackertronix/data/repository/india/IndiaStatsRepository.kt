package com.hackertronix.data.repository.india

import com.hackertronix.IndiaStatsRequestState
import com.hackertronix.IndiaStatsRequestState.Failure
import com.hackertronix.IndiaStatsRequestState.Loading
import com.hackertronix.IndiaStatsRequestState.Success
import com.hackertronix.IndiaStatsRequestState.SuccessWithoutResult
import com.hackertronix.data.local.Covid19StatsDatabase
import com.hackertronix.data.network.IndApi
import com.hackertronix.model.india.latest.LatestIndianStats
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class IndiaStatsRepository(
    private val indiaApiClient: IndApi,
    private val database: Covid19StatsDatabase
) {

    val emitter = PublishRelay.create<IndiaStatsRequestState>()
    private val disposables = CompositeDisposable()

    fun getLatestIndiaStats() {
        emitter.accept(Loading)
        disposables += getLatestIndiaStatsFromDb()
            .flatMap {
                if (it.isEmpty()) {
                    return@flatMap getLatestIndiaStatsFromApi()
                }
                return@flatMap Flowable.just(Success(it.first()))
            }
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = {
                    emitter.accept(it)
                },
                onError = {
                    emitter.accept(Failure(it.message!!))
                }
            )
    }

    private fun getLatestIndiaStatsFromDb(): Flowable<List<LatestIndianStats>> {
        return database.latestStatsDao().getLatestStats()
            .subscribeOn(Schedulers.io())
    }

    private fun getLatestIndiaStatsFromApi(): Flowable<IndiaStatsRequestState> {
        return indiaApiClient.getLatestStats()
            .map<IndiaStatsRequestState> {
                Success(it)
            }
            .onErrorReturn {
                Failure(it.message!!)
            }
            .subscribeOn(Schedulers.io())
            .doOnSuccess { requestState ->
                when (requestState) {
                    is Success -> saveToDisk(requestState.latest)
                }
            }.toFlowable()
    }

    private fun saveToDisk(latestStat: LatestIndianStats) {
        database.latestStatsDao().insertLatest(latestStat)
    }

    fun dispose() {
        disposables.dispose()
    }

    fun refreshLatestIndiaStats() {
        emitter.accept(Loading)
        disposables += getLatestIndiaStatsFromApi()
            .subscribeBy(
                onComplete = {
                    emitter.accept(SuccessWithoutResult)
                },
                onError = {
                    emitter.accept(Failure(it.message!!))
                })
    }
}