package com.hackertronix.divocstats.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hackertronix.data.repository.OverviewRepository
import com.hackertronix.divocstats.common.RefreshState
import com.hackertronix.divocstats.common.RefreshState.Done
import com.hackertronix.divocstats.common.RefreshState.Loading
import com.hackertronix.model.overview.Overview
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class OverviewViewModel(private val repository: OverviewRepository) : ViewModel() {

    private val overviewObservable: Observable<Overview> = repository.getOverview()
    private val overviewLiveData = MutableLiveData<Overview>()
    private val errorLiveData = MutableLiveData<String>()
    private val disposables = CompositeDisposable()
    private val refreshState = MutableLiveData<RefreshState>()

    fun getOverview(): LiveData<Overview> = overviewLiveData
    fun getRefreshState(): LiveData<RefreshState> = refreshState

    init {
        disposables += overviewObservable.subscribeBy(
            onNext = {
                overviewLiveData.postValue(it)
            },
            onError = {
                errorLiveData.postValue(it.message)
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    private fun refreshOverview() {
        disposables += repository.getOverviewFromApi()
            .subscribeBy(
                onError = {
                    errorLiveData.postValue(it.message)
                    refreshState.postValue(Done)
                },

                onComplete = {
                    refreshState.postValue(Done)
                }
            )
    }

    fun startRefresh() {
        refreshState.postValue(Loading)
        refreshOverview()
    }
}