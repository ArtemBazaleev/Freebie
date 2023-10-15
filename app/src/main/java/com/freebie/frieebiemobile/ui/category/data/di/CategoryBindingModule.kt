package com.freebie.frieebiemobile.ui.category.data.di

import com.freebie.frieebiemobile.ui.category.data.api.CategoryApi
import com.freebie.frieebiemobile.ui.category.data.api.CategoryApiImpl
import com.freebie.frieebiemobile.ui.category.data.repository.CategoryRepository
import com.freebie.frieebiemobile.ui.category.data.repository.CategoryRepositoryImpl
import com.freebie.frieebiemobile.ui.category.data.usecase.GetCategoryRepositoryUseCaseImpl
import com.freebie.frieebiemobile.ui.category.domain.usecase.GetCategoryRepositoryUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CategoryBindingModule {

    @Binds
    abstract fun provideCategoryApi(impl: CategoryApiImpl): CategoryApi

    @Binds
    abstract fun bindRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    abstract fun bindGetCategoryUseCase(impl: GetCategoryRepositoryUseCaseImpl): GetCategoryRepositoryUseCase

}