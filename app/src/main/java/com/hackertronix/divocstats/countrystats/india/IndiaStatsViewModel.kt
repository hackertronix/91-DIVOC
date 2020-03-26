package com.hackertronix.divocstats.countrystats.india

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hackertronix.IndiaStatsRequestState
import com.hackertronix.IndiaStatsRequestState.Failure
import com.hackertronix.IndiaStatsRequestState.Loading
import com.hackertronix.IndiaStatsRequestState.Success
import com.hackertronix.IndiaStatsRequestState.SuccessWithoutResult
import com.hackertronix.data.repository.india.IndiaStatsRepository
import com.hackertronix.divocstats.common.UiState
import com.hackertronix.model.india.latest.LatestIndianStats
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class IndiaStatsViewModel(private val repository: IndiaStatsRepository) : ViewModel() {

    private val latestStatsObservable: Observable<IndiaStatsRequestState> = repository.emitter
    private val latestStatsLiveData = MutableLiveData<LatestIndianStats>()

    private val disposables = CompositeDisposable()
    private val uiState = MutableLiveData<UiState>()

    fun getLatestStats(): LiveData<LatestIndianStats> = latestStatsLiveData
    fun getRefreshState(): LiveData<UiState> = uiState

    init {
        repository.getLatestIndiaStats()
        disposables += latestStatsObservable.subscribeBy(
            onNext = { state ->
                when (state) {
                    is Loading -> uiState.postValue(UiState.Loading)
                    is Failure -> uiState.postValue(UiState.Done)
                    is SuccessWithoutResult -> uiState.postValue(UiState.Done)
                    is Success -> {
                        latestStatsLiveData.postValue(state.latest)
                        uiState.postValue(UiState.Done)
                    }
                }
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
    fun refreshLatestStats() {
        repository.refreshLatestIndiaStats()
    }
}