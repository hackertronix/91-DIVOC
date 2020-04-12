package com.hackertronix.divocstats.countrystats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.hackertronix.CountryStatsRequestState
import com.hackertronix.CountryStatsRequestState.*
import com.hackertronix.data.repository.CountryStatsRepository
import com.hackertronix.divocstats.common.UiState
import com.hackertronix.divocstats.common.UiState.Done
import com.hackertronix.divocstats.parseUTCToLong
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

    private val latestStatsObservable: Observable<CountryStatsRequestState> =
        repository.emitter.startWith(Loading)

    private val latestStatsLiveData = MutableLiveData<List<Location>>()
    private val selectedCountryStatsLiveData = MutableLiveData<Location>()
    private val confirmedDataSet = MutableLiveData<LineDataSet>()
    private val deathsDataSet = MutableLiveData<LineDataSet>()
    private val disposables = CompositeDisposable()
    private val uiState = MutableLiveData<UiState>()

    private lateinit var selectedCountryCode: String

    init {
        disposables += latestStatsObservable.subscribeBy(
            onNext = { state ->
                when (state) {
                    is Loading -> uiState.postValue(UiState.Loading)
                    is Failure -> uiState.postValue(UiState.Done)
                    is SuccessWithoutResult -> uiState.postValue(UiState.Done)
                    is Success -> {
                        latestStatsLiveData.postValue(state.location)
                        uiState.postValue(UiState.Done)

                        viewModelScope.launch(Dispatchers.Default) {

                            flattenCountriesData(state.location, selectedCountryCode)
                            flattenConfirmedTimelines(state.location, selectedCountryCode)
                            flattenDeathsTimelines(state.location, selectedCountryCode)

                            uiState.postValue(Done)

                        }
                    }
                }
            }
        )
    }

    fun getLatestStatsForCountry(): LiveData<Location> = selectedCountryStatsLiveData
    fun getConfirmedDataset(): LiveData<LineDataSet> = confirmedDataSet
    fun getDeathsDataset(): LiveData<LineDataSet> = deathsDataSet
    fun getUiState(): LiveData<UiState> = uiState

    fun setCountryCode(countryCode: String) {
        this.selectedCountryCode = countryCode
        repository.getLatestCountriesStats(countryCode)
    }

    private fun flattenConfirmedTimelines(listOfLocations: List<Location>, countryCode: String) {
        listOfLocations.filter { location ->
            location.countryCode == countryCode
        }.maxBy {
            it.latest.confirmed
        }?.let { location ->

            val listOfConfirmedTimelines = location.timelines.confirmed.timelineConfirmed
            val entries = mutableListOf<Entry>()
            for ((x, y) in listOfConfirmedTimelines) {
                val entry = Entry(x.parseUTCToLong().toFloat(), y.toFloat())
                entries.add(entry)
            }
            confirmedDataSet.postValue(LineDataSet(entries, "confirmed"))
        }
    }

    private fun flattenDeathsTimelines(listOfLocations: List<Location>, countryCode: String) {
        listOfLocations.filter { location ->
            location.countryCode == countryCode
        }.maxBy {
            it.latest.confirmed
        }?.let { location ->

            val listOfDeathsTimelines = location.timelines.deaths.timelineDeaths
            val entries = mutableListOf<Entry>()
            for ((x, y) in listOfDeathsTimelines) {
                val entry = Entry(x.parseUTCToLong().toFloat(), y.toFloat())
                entries.add(entry)
            }

            deathsDataSet.postValue(LineDataSet(entries, "deaths"))
        }
    }

    private fun flattenCountriesData(
        listOfLocations: List<Location>,
        countryCode: String
    ) {
        listOfLocations.filter { location ->
            location.countryCode == countryCode
        }.maxBy {
            it.latest.confirmed
        }?.let {
            selectedCountryStatsLiveData.postValue(it)
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