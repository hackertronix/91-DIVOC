package com.hackertronix.divocstats.common

sealed class UiState {
    object Loading : UiState()
    object Done : UiState()
    data class Error(val error: String) : UiState()
}