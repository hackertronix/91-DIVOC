package com.hackertronix

import com.hackertronix.model.countries.Location
import com.hackertronix.model.global.overview.Overview
import com.hackertronix.model.india.latest.LatestIndianStats

sealed class OverviewRequestState {
    object Loading : OverviewRequestState()
    object SuccessWithoutResult : OverviewRequestState()
    data class Success(val overview: Overview) : OverviewRequestState()
    data class Failure(val error: String) : OverviewRequestState()
}

sealed class IndiaStatsRequestState {
    object Loading : IndiaStatsRequestState()
    object SuccessWithoutResult : IndiaStatsRequestState()
    data class Success(val latest: LatestIndianStats) : IndiaStatsRequestState()
    data class Failure(val error: String) : IndiaStatsRequestState()
}

sealed class CountryStatsRequestState {
    object Loading : CountryStatsRequestState()
    object SuccessWithoutResult : CountryStatsRequestState()
    data class Success(val location: List<Location>) : CountryStatsRequestState()
    data class Failure(val error: String) : CountryStatsRequestState()
}

