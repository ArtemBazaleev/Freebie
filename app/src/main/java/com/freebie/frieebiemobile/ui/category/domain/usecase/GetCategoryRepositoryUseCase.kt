package com.freebie.frieebiemobile.ui.category.domain.usecase

import com.freebie.frieebiemobile.ui.category.domain.model.CategoryModel

interface GetCategoryRepositoryUseCase {
    suspend operator fun invoke(): Result<List<CategoryModel>>
}