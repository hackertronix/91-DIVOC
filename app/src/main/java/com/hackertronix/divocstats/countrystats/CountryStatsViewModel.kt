package com.hackertronix.divocstats.countrystats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hackertronix.data.repository.india.LatestStatsRepository
import com.hackertronix.model.india.latest.Latest
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class CountryStatsViewModel(val repository: LatestStatsRepository) : ViewModel() {

    private val latestStatsObservable: Observable<Latest> = repository.getLatestStats()
    private val latestStats = MutableLiveData<Latest>()
    private val errorLiveData = MutableLiveData<String>()
    private val disposables = CompositeDisposable()

    init {
        disposables += latestStatsObservable.subscribeBy(
            onNext = {
                latestStats.postValue(it)
            },
            onError = {
                errorLiveData.postValue(it.message)
            }
        )
    }

    fun getLatestStats(): LiveData<Latest> = latestStats

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}