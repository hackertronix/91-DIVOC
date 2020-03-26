package com.hackertronix.data.repository

import com.hackertronix.CountriesStatsRequestState
import com.hackertronix.CountriesStatsRequestState.Failure
import com.hackertronix.CountriesStatsRequestState.Loading
import com.hackertronix.CountriesStatsRequestState.Success
import com.hackertronix.CountriesStatsRequestState.SuccessWithoutResult
import com.hackertronix.data.local.Covid19StatsDatabase
import com.hackertronix.data.network.TimelinesApi
import com.hackertronix.model.countries.CountriesStats
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class CountryStatsRepository(
    private val apiClient: TimelinesApi,
    private val database: Covid19StatsDatabase
) {

    val emitter = PublishRelay.create<CountriesStatsRequestState>()
    private val disposables = CompositeDisposable()

    fun getLatestCountriesStats() {
        emitter.accept(Loading)
        disposables += getLatestCountriesStatsFromDb()
            .flatMap {
                if (it.isEmpty()) {
                    return@flatMap getLatestCountriesStatsFromApi()
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

    private fun getLatestCountriesStatsFromDb(): Flowable<List<CountriesStats>> {
        return database.countriesStatsDao().getCountriesStats()
            .subscribeOn(Schedulers.io())
    }

    private fun getLatestCountriesStatsFromApi(): Flowable<CountriesStatsRequestState> {
        return apiClient.getTimelinesForAllCountries()
            .map<CountriesStatsRequestState> {
                Success(it)
            }
            .onErrorReturn {
                Failure(it.message!!)
            }
            .subscribeOn(Schedulers.io())
            .doOnSuccess { requestState ->
                when (requestState) {
                    is Success -> saveToDisk(requestState.countriesStats)
                }
            }.toFlowable()
    }

    private fun saveToDisk(countriesStats: CountriesStats) {
        database.countriesStatsDao().insertCountryStats(countriesStats)
    }

    fun dispose() {
        disposables.dispose()
    }

    fun refreshLatestCountiesStats() {
        emitter.accept(Loading)
        disposables += getLatestCountriesStatsFromApi()
            .subscribeBy(
                onComplete = {
                    emitter.accept(SuccessWithoutResult)
                },
                onError = {
                    emitter.accept(Failure(it.message!!))
                })
    }
}