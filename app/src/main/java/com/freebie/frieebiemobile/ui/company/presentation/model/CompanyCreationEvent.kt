package com.freebie.frieebiemobile.ui.company.presentation.model

import com.freebie.frieebiemobile.ui.company.domain.model.CompanyEditModel

sealed interface CompanyCreationEvent {
    class CompanyInfoReceived(val model: CompanyEditModel) : CompanyCreationEvent
    object ErrorWhileCreatingCompany : CompanyCreationEvent
    object ErrorWhileGettingCompanyInfo : CompanyCreationEvent
    object CloseSelf : CompanyCreationEvent
}