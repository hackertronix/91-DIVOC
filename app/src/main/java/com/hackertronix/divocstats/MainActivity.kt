package com.hackertronix.divocstats

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.hackertronix.divocstats.countrystats.CountryStatsFragment
import com.hackertronix.divocstats.countrystats.CountryStatsFragment.Companion.INDIA
import com.hackertronix.divocstats.countrystats.india.IndiaStatsFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private var drawableId: Int = R.drawable.ic_night_mode
    private var darkModeOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSelectedThemeFromPrefs()
    }

    private fun getSelectedThemeFromPrefs() {
        when (getSharedPreferences(DARK_MODE, MODE_PRIVATE).getInt(DARK_MODE_ON, 1)) {
            0 -> {
                darkModeOn = false
                drawableId = R.drawable.ic_night_mode
            }
            1 -> {
                darkModeOn = true
                drawableId = R.drawable.ic_brightness_7_black_24dp
            }
        }

    }

    fun showCountryStats(countryCode: String) {
        val fragmentManager = supportFragmentManager
        var countryStatsFragment = fragmentManager.findFragmentByTag(COUNTRY_STATS_FRAGMENT)
        if (countryStatsFragment == null) {

            countryStatsFragment = when (countryCode) {
                INDIA -> IndiaStatsFragment.newInstance(countryCode)
                else -> CountryStatsFragment.newInstance(countryCode)
            }
            fragmentManager.beginTransaction()
                .replace(R.id.container, countryStatsFragment, COUNTRY_STATS_FRAGMENT)
                .addToBackStack(COUNTRY_STATS_FRAGMENT)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu?.get(0)?.icon = resources.getDrawable(drawableId, theme)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                handleOnBackPressed()
            }

            R.id.menu_night_mode -> {
                changeTheme()
            }
        }

        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount >= 1) {
            invalidateOptionsMenu()
        }
        super.onBackPressed()
    }

    private fun handleOnBackPressed() {
        if (supportFragmentManager.backStackEntryCount >= 1) {
            invalidateOptionsMenu()
        }
        onBackPressed()
    }

    private fun changeTheme() {
        if (darkModeOn) {
            getSharedPreferences(DARK_MODE, Context.MODE_PRIVATE).edit().putInt(DARK_MODE_ON, 0).apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            return
        }

        getSharedPreferences(DARK_MODE, Context.MODE_PRIVATE).edit().putInt(DARK_MODE_ON, 1).apply()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    companion object {
        const val COUNTRY_STATS_FRAGMENT = "country_stats_fragment"
        const val DARK_MODE = "dark_mode"
        const val DARK_MODE_ON = "dark_mode_on"
    }
}
