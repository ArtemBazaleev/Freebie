package com.freebie.frieebiemobile.ui.category.mapper

import android.graphics.Color
import com.freebie.frieebiemobile.ui.category.domain.model.CategoryModel
import com.freebie.frieebiemobile.ui.category.model.CategoryUI
import javax.inject.Inject

class CategoryMapper @Inject constructor(

) {
    fun mapToUi(model: CategoryModel) : CategoryUI {
        return CategoryUI(
            id = model.categoryId,
            name = model.categoryName,
            color = Color.parseColor(model.categoryColor),
            image = model.categoryIcon
        )
    }
}