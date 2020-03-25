package com.hackertronix.divocstats.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.hackertronix.OverviewRequestState
import com.hackertronix.OverviewRequestState.Failure
import com.hackertronix.OverviewRequestState.Loading
import com.hackertronix.OverviewRequestState.Success
import com.hackertronix.OverviewRequestState.SuccessWithoutResult
import com.hackertronix.data.repository.OverviewRepository
import com.hackertronix.divocstats.common.UiState
import com.hackertronix.divocstats.parseDateToLong
import com.hackertronix.model.global.daily.DailyStats
import com.hackertronix.model.global.overview.Overview
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat

class OverviewViewModel(private val repository: OverviewRepository) : ViewModel() {

    private val overviewObservable: Observable<OverviewRequestState> = repository.overviewEmitter
    private val dailyStatsObservable: Observable<List<DailyStats>> = repository.dailyStatsEmitter
    private val overviewLiveData = MutableLiveData<Overview>()
    private val confirmedDataSetLiveData = MutableLiveData<LineDataSet>()

    private val disposables = CompositeDisposable()
    private val uiState = MutableLiveData<UiState>()

    fun getOverview(): LiveData<Overview> = overviewLiveData
    fun getUiState(): LiveData<UiState> = uiState
    fun getConfirmedDataSet(): LiveData<LineDataSet> = confirmedDataSetLiveData

    init {
        repository.getOverview()
        repository.getDailyStats()

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

        disposables += dailyStatsObservable.subscribeBy(
            onNext = { dailyStats ->
                val formatter = DateTimeFormat.forPattern("yyyy-mm-dd")
                val entries = mutableListOf<Entry>()
                viewModelScope.launch(Dispatchers.IO) {
                    dailyStats.forEach {stat ->
                        val entry = Entry(stat.reportDate.parseDateToLong().toFloat(),stat.totalConfirmed.toFloat())
                        entries.add(entry)
                    }
                    confirmedDataSetLiveData.postValue(LineDataSet(entries,"confirmed"))
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