package com.freebie.frieebiemobile.ui.category.data.api

import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.frieebiemobile.ui.category.data.model.CategoriesResponse
import com.freebie.frieebiemobile.ui.category.data.model.CategoryResponse
import com.freebie.protos.CategoryApiProtos
import com.freebie.protos.CategoryApiProtos.CategoryListRequest
import javax.inject.Inject

interface CategoryApi {
    suspend fun requestCategories(): CategoriesResponse
}

class CategoryApiImpl @Inject constructor(
    private val httpAccess: HttpAccess
): CategoryApi {

    override suspend fun requestCategories(): CategoriesResponse {
        val request = CategoryListRequest
            .newBuilder()
            .setSize(20)
            .setPage(0)
            .build()
        val response = httpAccess.httpRequest(
            requestUrlSegment = CATEGORY_LIST_API,
            headers = emptyMap(),
            body = request.toByteArray(),
            method = Method.POST
        )
        val proto = CategoryApiProtos.CategoryListResponse.parseFrom(response.bodyAsArray)
        return CategoriesResponse(
            categories = proto.categoriesList.map { protoCategory ->
                CategoryResponse(
                    categoryId = protoCategory.encryptedId,
                    categoryName = protoCategory.name,
                    categoryColor = protoCategory.color,
                    categoryIcon = protoCategory.imageUrl
                )
            }
        )
    }

    companion object {
        private const val CATEGORY_LIST_API = "v1/categories"
    }

}