package com.hackertronix.divocstats

import android.app.Application
import android.content.Context
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.ExistingPeriodicWorkPolicy.KEEP
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.hackertronix.data.worker.IndiaStatsWorker
import com.hackertronix.data.worker.OverviewWorker
import com.hackertronix.di.databaseModule
import com.hackertronix.di.networkModule
import com.hackertronix.di.repositoryModule
import com.hackertronix.divocstats.countrystats.CountryStatsFragment.Companion.INDIA
import com.hackertronix.divocstats.di.adaptersModule
import com.hackertronix.divocstats.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit.HOURS

class DivocApp : Application() {

    override fun onCreate() {
        super.onCreate()

        setTheme()
        startKoin {
            androidContext(this@DivocApp)
            modules(listOf(networkModule, databaseModule, repositoryModule, adaptersModule, viewModelModule))
        }

        scheduleWork()
    }

    private fun setTheme() {
        when (getSharedPreferences(
            MainActivity.DARK_MODE,
            AppCompatActivity.MODE_PRIVATE
        ).getInt(MainActivity.DARK_MODE_ON, 1)) {

            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun scheduleWork() {
        val request = PeriodicWorkRequestBuilder<OverviewWorker>(1, HOURS).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("OverviewWorker", KEEP, request)

        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = telephonyManager.simCountryIso.toUpperCase()

        when (countryCode) {
            INDIA -> {
                val indiaStatsRequest = PeriodicWorkRequestBuilder<IndiaStatsWorker>(1, HOURS).build()
                WorkManager.getInstance(applicationContext)
                    .enqueueUniquePeriodicWork("LatestStatsWorker", KEEP, indiaStatsRequest)
            }
            else ->{
                val latestStatsRequest = PeriodicWorkRequestBuilder<IndiaStatsWorker>(1, HOURS).build()
                WorkManager.getInstance(applicationContext)
                    .enqueueUniquePeriodicWork("LatestStatsWorker", KEEP, latestStatsRequest)

            }
        }
    }
}