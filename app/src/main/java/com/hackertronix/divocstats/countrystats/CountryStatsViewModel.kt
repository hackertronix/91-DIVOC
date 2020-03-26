package com.hackertronix.divocstats.countrystats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.hackertronix.CountriesStatsRequestState
import com.hackertronix.CountriesStatsRequestState.Failure
import com.hackertronix.CountriesStatsRequestState.Loading
import com.hackertronix.CountriesStatsRequestState.Success
import com.hackertronix.CountriesStatsRequestState.SuccessWithoutResult
import com.hackertronix.data.repository.CountryStatsRepository
import com.hackertronix.divocstats.common.UiState
import com.hackertronix.divocstats.common.UiState.Done
import com.hackertronix.divocstats.parseUTCToLong
import com.hackertronix.model.countries.CountriesStats
import com.hackertronix.model.countries.Latest
import com.hackertronix.model.countries.Location
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountryStatsViewModel(
    private val repository: CountryStatsRepository
) : ViewModel() {

    private val latestStatsObservable: Observable<CountriesStatsRequestState> = repository.emitter

    private val latestStatsLiveData = MutableLiveData<CountriesStats>()
    private val selectedCountryStatsLiveData = MutableLiveData<Location>()

    private val confirmedDataSet = MutableLiveData<LineDataSet>()
    private val deathsDataSet = MutableLiveData<LineDataSet>()
    private val disposables = CompositeDisposable()
    private val uiState = MutableLiveData<UiState>()

    private lateinit var selectedCountryCode: String

    init {
        repository.getLatestCountriesStats()
        disposables += latestStatsObservable.subscribeBy(
            onNext = { state ->
                when (state) {
                    is Loading -> uiState.postValue(UiState.Loading)
                    is Failure -> uiState.postValue(UiState.Done)
                    is SuccessWithoutResult -> uiState.postValue(UiState.Done)
                    is Success -> {
                        latestStatsLiveData.postValue(state.countriesStats)
                        uiState.postValue(UiState.Done)
                    }
                }
            }
        )
    }

    fun getLatestStats(): LiveData<CountriesStats> = latestStatsLiveData
    fun getLatestStatsForCountry(): LiveData<Location> = selectedCountryStatsLiveData
    fun getConfirmedDataset(): LiveData<LineDataSet> = confirmedDataSet
    fun getDeathsDataset(): LiveData<LineDataSet> = deathsDataSet
    fun getUiState(): LiveData<UiState> = uiState

    fun setCountryCode(countryCode: String) {
        this.selectedCountryCode = countryCode

        disposables += latestStatsObservable.subscribeBy(
            onNext = { state ->
                when (state) {
                    is Loading -> uiState.postValue(UiState.Loading)
                    is Failure -> uiState.postValue(UiState.Done)
                    is SuccessWithoutResult -> uiState.postValue(UiState.Done)
                    is Success -> {
                        viewModelScope.launch(Dispatchers.Default) {

                            flattenCountriesData(state.countriesStats.locations, countryCode)
                            flattenConfirmedTimelines(state.countriesStats.locations, countryCode)
                            flattenDeathsTimelines(state.countriesStats.locations, countryCode)

                            uiState.postValue(Done)

                        }
                    }
                }
            }
        )
    }

    private fun flattenConfirmedTimelines(listOfLocations: List<Location>, countryCode: String) {
        listOfLocations.filter { location ->
            location.countryCode == countryCode
        }.let { locations ->

            locations.run {
                val listOfConfirmedTimelines = map { it.timelines.confirmed.timelineConfirmed }
                val entries = mutableListOf<Entry>()
                listOfConfirmedTimelines.forEach {  map ->
                    for ((x, y) in map) {
                        val entry = Entry(x.parseUTCToLong().toFloat(), y.toFloat())
                        entries.add(entry)
                    }
                }
                confirmedDataSet.postValue(LineDataSet(entries, "confirmed"))
            }
        }
    }
    private fun flattenDeathsTimelines(listOfLocations: List<Location>, countryCode: String) {
        listOfLocations.filter { location ->
            location.countryCode == countryCode
        }.let { locations ->

            locations.run {
                val listOfDeathsTimelines = map { it.timelines.deaths.timelineDeaths }
                val entries = mutableListOf<Entry>()
                listOfDeathsTimelines.forEach { map ->
                    for ((x, y) in map) {
                        val entry = Entry(x.parseUTCToLong().toFloat(), y.toFloat())
                        entries.add(entry)
                    }
                }
                deathsDataSet.postValue(LineDataSet(entries, "deaths"))
            }
        }
    }

    private fun flattenCountriesData(
        listOfLocations: List<Location>,
        countryCode: String
    ) {
        listOfLocations.filter { location ->
            location.countryCode == countryCode
        }.let { locations ->

            locations.run {
                val totalConfirmed = map { it.latest.confirmed }.sum()
                val totalRecovered = map { it.latest.recovered }.sum()
                val totalDeaths = map { it.latest.deaths }.sum()

                val updatedAt = maxBy { it.lastUpdated }!!.lastUpdated
                val latest = Latest(
                    confirmed = totalConfirmed,
                    recovered = totalRecovered, deaths = totalDeaths
                )
                val location = Location(
                    country = first().country,
                    countryCode = first().countryCode,
                    lastUpdated = updatedAt,
                    latest = latest
                )
                selectedCountryStatsLiveData.postValue(location)
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun refreshLatestStats() {
        repository.refreshLatestCountiesStats()
    }
}