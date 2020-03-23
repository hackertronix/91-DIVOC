package com.hackertronix.data.repository

import com.hackertronix.OverviewRequestState
import com.hackertronix.OverviewRequestState.Failure
import com.hackertronix.OverviewRequestState.Loading
import com.hackertronix.OverviewRequestState.Success
import com.hackertronix.OverviewRequestState.SuccessWithoutResult
import com.hackertronix.data.local.Covid19StatsDatabase
import com.hackertronix.data.network.API
import com.hackertronix.model.overview.Overview
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

    val emitter = PublishRelay.create<OverviewRequestState>()
    private val disposables = CompositeDisposable()

    fun getOverview() {
        emitter.accept(Loading)
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
                    emitter.accept(requestState)
                },
                onError = {
                    emitter.accept(Failure(it.message!!))
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
        emitter.accept(Loading)
        disposables += getOverviewFromApi()
            .subscribeBy(
                onComplete = {
                    emitter.accept(SuccessWithoutResult)
                },
                onError = {
                    emitter.accept(Failure(it.message!!))
                })
    }
}
