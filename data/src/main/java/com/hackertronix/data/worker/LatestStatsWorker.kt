package com.hackertronix.data.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.hackertronix.data.local.Covid19StatsDatabase
import com.hackertronix.data.network.IndApi
import com.hackertronix.model.india.latest.Latest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class LatestStatsWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params), KoinComponent {

    private val disposables = CompositeDisposable()
    private val apiClient: IndApi by inject()
    private val databaseClient: Covid19StatsDatabase by inject()

    override fun doWork(): Result {
        disposables += apiClient.getLatestStats()
            .map {
                deleteAndSaveLatestStats(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Log.d("LatestStats Worker", "Failed ${it.localizedMessage}")
                },

                onSuccess = {
                    Log.d("LatestStats Worker", "Saved data")
                }
            )

        return Result.success()
    }

    private fun deleteAndSaveLatestStats(latestStats: Latest) {
        Log.d("LatestStats Worker", "Saving data")
        databaseClient.latestStatsDao().deleteLatest()
        databaseClient.latestStatsDao().insertLatest(latestStats)
    }

    override fun onStopped() {
        disposables.clear()
        super.onStopped()
    }
}