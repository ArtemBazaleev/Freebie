package com.freebie.frieebiemobile.ui.category.model

import com.freebie.frieebiemobile.ui.feed.models.PlaceHolderInfo

data class CategoryState(
    val categories: List<CategoryItem>,
    val placeholder: PlaceHolderInfo
)