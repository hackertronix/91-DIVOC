package com.hackertronix.divocstats.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hackertronix.OverviewRequestState
import com.hackertronix.OverviewRequestState.Failure
import com.hackertronix.OverviewRequestState.Loading
import com.hackertronix.OverviewRequestState.Success
import com.hackertronix.OverviewRequestState.SuccessWithoutResult
import com.hackertronix.data.repository.OverviewRepository
import com.hackertronix.divocstats.common.UiState
import com.hackertronix.model.overview.Overview
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class OverviewViewModel(private val repository: OverviewRepository) : ViewModel() {

    private val overviewObservable: Observable<OverviewRequestState> = repository.emitter
    private val overviewLiveData = MutableLiveData<Overview>()

    private val disposables = CompositeDisposable()
    private val uiState = MutableLiveData<UiState>()

    fun getOverview(): LiveData<Overview> = overviewLiveData
    fun getUiState(): LiveData<UiState> = uiState

    init {
        repository.getOverview()
        disposables += overviewObservable.subscribeBy(
            onNext = { state ->
                when (state) {
                    is Loading -> uiState.postValue(UiState.Loading)
                    is Failure -> uiState.postValue(UiState.Done)
                    is SuccessWithoutResult -> uiState.postValue(UiState.Done)
                    is Success -> {
                        overviewLiveData.postValue(state.overview)
                        uiState.postValue(UiState.Done)
                    }
                }
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        repository.dispose()
    }

    fun refreshOverview() {
        repository.refreshOverview()
    }
}