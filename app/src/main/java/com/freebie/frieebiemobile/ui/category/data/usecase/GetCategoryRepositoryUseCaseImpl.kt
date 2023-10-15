package com.freebie.frieebiemobile.ui.category.data.usecase

import com.freebie.frieebiemobile.ui.category.data.repository.CategoryRepository
import com.freebie.frieebiemobile.ui.category.domain.model.CategoryModel
import com.freebie.frieebiemobile.ui.category.domain.usecase.GetCategoryRepositoryUseCase
import javax.inject.Inject

class GetCategoryRepositoryUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
): GetCategoryRepositoryUseCase {
    override suspend fun invoke(): Result<List<CategoryModel>> = repository.getCategoryList()
}