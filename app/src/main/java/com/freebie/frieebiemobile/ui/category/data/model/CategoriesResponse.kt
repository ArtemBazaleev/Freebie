package com.freebie.frieebiemobile.ui.category.data.model

data class CategoriesResponse(
    val categories: List<CategoryResponse>
)

data class CategoryResponse(
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val categoryColor: String
)