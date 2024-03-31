package com.freebie.frieebiemobile.ui.company.presentation.model

sealed interface CompanyCreationEvent {
    object ErrorWhileCreatingCompany : CompanyCreationEvent
    object CloseSelf : CompanyCreationEvent
}