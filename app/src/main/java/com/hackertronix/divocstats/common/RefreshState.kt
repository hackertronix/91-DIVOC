package com.hackertronix.divocstats.common

sealed class RefreshState {
    object Loading : RefreshState()
    object Done : RefreshState()
}