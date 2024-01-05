package com.freebie.frieebiemobile.ui.rate.presentation.model

sealed interface RateCompanyEvent {
    object RateSuccess: RateCompanyEvent
    object RateFailed: RateCompanyEvent
}