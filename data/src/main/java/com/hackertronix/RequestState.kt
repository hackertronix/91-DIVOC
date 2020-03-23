package com.hackertronix

import com.hackertronix.model.india.latest.Latest
import com.hackertronix.model.overview.Overview

sealed class OverviewRequestState {
    object Loading : OverviewRequestState()
    object SuccessWithoutResult: OverviewRequestState()
    data class Success(val overview: Overview) : OverviewRequestState()
    data class Failure(val error: String) : OverviewRequestState()
}

sealed class LatestStatsRequestState {
    object Loading : LatestStatsRequestState()
    object SuccessWithoutResult: LatestStatsRequestState()
    data class Success(val latest: Latest) : LatestStatsRequestState()
    data class Failure(val error: String) : LatestStatsRequestState()
}

