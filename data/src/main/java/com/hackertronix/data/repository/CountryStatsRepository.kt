package com.hackertronix.data.repository

import com.hackertronix.CountryStatsRequestState
import com.hackertronix.CountryStatsRequestState.*
import com.hackertronix.data.local.Covid19StatsDatabase
import com.hackertronix.data.network.TimelinesApi
import com.hackertronix.model.countries.Location
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

    val emitter = PublishRelay.create<CountryStatsRequestState>()
    private val disposables = CompositeDisposable()

    fun getLatestCountriesStats(countryCode: String) {
        emitter.accept(Loading)
        disposables += getLatestCountryStatFromDb(countryCode)
            .flatMap { locations ->
                if (locations.isEmpty()) {
                    return@flatMap getLatestCountriesStatsFromApi()
                }
                return@flatMap Flowable.just(Success(locations))
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

    private fun getLatestCountryStatFromDb(countryCode: String): Flowable<List<Location>> {
        return database.countriesStatsDao().getCountryStatsForCountryCode(countryCode)
            .subscribeOn(Schedulers.io())
    }

    private fun getLatestCountriesStatsFromApi(): Flowable<CountryStatsRequestState> {
        return apiClient.getTimelinesForAllCountries()
            .map<CountryStatsRequestState> {
                saveToDisk(it.locations)
                return@map SuccessWithoutResult
            }
            .onErrorReturn {
                it.printStackTrace()
                Failure(it.message!!)
            }
            .subscribeOn(Schedulers.io())
            .toFlowable()
    }

    private fun saveToDisk(countriesStats: List<Location>) {
        database.countriesStatsDao().insertLocations(countriesStats)
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