package com.freebie.frieebiemobile.ui.company.presentation.model

import androidx.annotation.StringRes
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.category.model.CategoryUI

data class CompanyCreationUiModel (
    val categories: List<CategoryUI>,
    val cities: List<String>,
    val errorCompanyName: CreateCompanyFieldError? = null,
    val errorDescription: CreateCompanyFieldError? = null,
    val errorCategory: CreateCompanyFieldError? = null
)

enum class CreateCompanyFieldError(
    @StringRes
    val resId: Int
) {
    FIELD_EMPTY(R.string.field_empty)
}