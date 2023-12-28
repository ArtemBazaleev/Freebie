package com.freebie.frieebiemobile.ui.booklet.data.di

import com.freebie.frieebiemobile.ui.booklet.data.api.BookletApi
import com.freebie.frieebiemobile.ui.booklet.data.api.BookletApiImpl
import com.freebie.frieebiemobile.ui.booklet.data.repository.BookletRepository
import com.freebie.frieebiemobile.ui.booklet.data.repository.BookletRepositoryImpl
import com.freebie.frieebiemobile.ui.booklet.data.usecase.GetBookletsByAccountUseCaseImpl
import com.freebie.frieebiemobile.ui.booklet.domain.GetBookletsByAccountUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface BookletModule {
    @Binds
    fun bindApi(impl: BookletApiImpl) : BookletApi
    @Binds
    fun bindRepo(impl: BookletRepositoryImpl): BookletRepository
    @Binds
    fun bindGetByAccountUseCase(impl: GetBookletsByAccountUseCaseImpl): GetBookletsByAccountUseCase
}