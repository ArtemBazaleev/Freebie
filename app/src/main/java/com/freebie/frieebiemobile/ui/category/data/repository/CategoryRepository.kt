package com.freebie.frieebiemobile.ui.category.data.repository

import com.freebie.frieebiemobile.ui.category.data.api.CategoryApi
import com.freebie.frieebiemobile.ui.category.domain.model.CategoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CategoryRepository {
    suspend fun getCategoryList(): Result<List<CategoryModel>>
}

class CategoryRepositoryImpl @Inject constructor(
    private val categoryApi: CategoryApi
) : CategoryRepository {

    override suspend fun getCategoryList(): Result<List<CategoryModel>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = categoryApi.requestCategories().categories.map {
                CategoryModel(
                    categoryId = it.categoryId,
                    categoryName = it.categoryName,
                    categoryColor = it.categoryColor,
                    categoryIcon = it.categoryIcon
                )
            }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}