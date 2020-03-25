package com.hackertronix.data.repository.india

import com.hackertronix.LatestStatsRequestState
import com.hackertronix.LatestStatsRequestState.Failure
import com.hackertronix.LatestStatsRequestState.Loading
import com.hackertronix.LatestStatsRequestState.Success
import com.hackertronix.LatestStatsRequestState.SuccessWithoutResult
import com.hackertronix.data.local.Covid19StatsDatabase
import com.hackertronix.data.network.API
import com.hackertronix.data.network.IndApi
import com.hackertronix.model.india.latest.Latest
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class LatestStatsRepository(
    private val indiaApiClient: IndApi,
    private val apiClient: API,
    private val database: Covid19StatsDatabase
) {

    val emitter = PublishRelay.create<LatestStatsRequestState>()
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

    private fun getLatestIndiaStatsFromDb(): Flowable<List<Latest>> {
        return database.latestStatsDao().getLatestStats()
            .subscribeOn(Schedulers.io())
    }

    private fun getLatestIndiaStatsFromApi(): Flowable<LatestStatsRequestState> {
        return indiaApiClient.getLatestStats()
            .map<LatestStatsRequestState> {
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

    private fun saveToDisk(latestStat: Latest) {
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

    fun getLatestStats() {
        TODO("Not yet implemented")
    }

    private fun getLatestStatsFromDb() {
        TODO("Not yet implemented")
    }

    private fun getLatestStatsFromApi() {
        TODO("Not yet implemented")
    }
    private fun saveToDisk() {
        TODO("Not yet implemented")
    }

    fun refreshLatestStats() {
        TODO("Not yet implemented")
    }
}