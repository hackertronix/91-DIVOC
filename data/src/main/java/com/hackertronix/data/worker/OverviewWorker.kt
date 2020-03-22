package com.hackertronix.data.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.hackertronix.data.local.Covid19StatsDatabase
import com.hackertronix.data.network.API
import com.hackertronix.model.overview.Overview
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class OverviewWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params), KoinComponent {

    private val disposables = CompositeDisposable()
    private val apiClient: API by inject()
    private val databaseClient: Covid19StatsDatabase by inject()

    override fun doWork(): Result {
        disposables += apiClient.getOverview()
            .map {
                deleteAndSaveOverview(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Log.d("Overview Worker", "Failed ${it.localizedMessage}")
                },

                onSuccess = {
                    Log.d("Overview Worker", "Saved data")
                }
            )

        return Result.success()
    }

    private fun deleteAndSaveOverview(it: Overview) {
        Log.d("Overview Worker", "Saving data")
        databaseClient.overviewDao().deleteOverview()
        databaseClient.overviewDao().insertOverview(it)
    }

    override fun onStopped() {
        disposables.clear()
        super.onStopped()
    }
}