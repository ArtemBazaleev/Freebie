package com.freebie.frieebiemobile.ui.company.presentation.model

import com.freebie.frieebiemobile.ui.category.model.CategoryUI

data class CompanyCreationUiModel (
    val categories: List<CategoryUI>,
    val cities: List<String>
)
