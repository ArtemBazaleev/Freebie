package com.freebie.frieebiemobile.ui.category.model


sealed interface CategoryItem {
    fun getItemViewType(): Int
}

data class CategoryUI(
    val id: String,
    val name: String,
    val color: Int,
    val image: String,
) : CategoryItem {
    override fun getItemViewType() = CategoryViewType.CATEGORY.ordinal
}

object ShimmerCategory : CategoryItem {
    override fun getItemViewType() = CategoryViewType.SHIMMER.ordinal
}


enum class CategoryViewType {
    CATEGORY, SHIMMER
}