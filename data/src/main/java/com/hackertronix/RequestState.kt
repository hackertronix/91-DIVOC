package com.hackertronix

import com.hackertronix.model.countries.CountriesStats
import com.hackertronix.model.india.latest.LatestIndianStats
import com.hackertronix.model.global.overview.Overview

sealed class OverviewRequestState {
    object Loading : OverviewRequestState()
    object SuccessWithoutResult: OverviewRequestState()
    data class Success(val overview: Overview) : OverviewRequestState()
    data class Failure(val error: String) : OverviewRequestState()
}

sealed class IndiaStatsRequestState {
    object Loading : IndiaStatsRequestState()
    object SuccessWithoutResult: IndiaStatsRequestState()
    data class Success(val latest: LatestIndianStats) : IndiaStatsRequestState()
    data class Failure(val error: String) : IndiaStatsRequestState()
}

sealed class CountriesStatsRequestState {
    object Loading : CountriesStatsRequestState()
    object SuccessWithoutResult: CountriesStatsRequestState()
    data class Success(val countriesStats: CountriesStats) : CountriesStatsRequestState()
    data class Failure(val error: String) : CountriesStatsRequestState()
}

