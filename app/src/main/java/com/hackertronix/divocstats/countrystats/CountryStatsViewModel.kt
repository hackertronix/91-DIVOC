package com.hackertronix.divocstats.countrystats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hackertronix.LatestStatsRequestState
import com.hackertronix.LatestStatsRequestState.Failure
import com.hackertronix.LatestStatsRequestState.Loading
import com.hackertronix.LatestStatsRequestState.Success
import com.hackertronix.LatestStatsRequestState.SuccessWithoutResult
import com.hackertronix.data.repository.india.LatestStatsRepository
import com.hackertronix.divocstats.common.UiState
import com.hackertronix.model.india.latest.Latest
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class CountryStatsViewModel(private val repository: LatestStatsRepository) : ViewModel() {

    private val latestStatsObservable: Observable<LatestStatsRequestState> = repository.emitter
    private val latestStatsLiveData = MutableLiveData<Latest>()

    private val disposables = CompositeDisposable()
    private val uiState = MutableLiveData<UiState>()

    fun getLatestStats(): LiveData<Latest> = latestStatsLiveData
    fun getRefreshState(): LiveData<UiState> = uiState

    init {
        repository.getLatestStats()
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
        repository.refreshLatestStats()
    }
}