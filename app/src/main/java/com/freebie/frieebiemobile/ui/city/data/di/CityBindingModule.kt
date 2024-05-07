package com.freebie.frieebiemobile.ui.city.data.di

import com.freebie.frieebiemobile.ui.city.data.api.CityApi
import com.freebie.frieebiemobile.ui.city.data.api.CityApiImpl
import com.freebie.frieebiemobile.ui.city.data.repository.CityRepository
import com.freebie.frieebiemobile.ui.city.data.repository.CityRepositoryImpl
import com.freebie.frieebiemobile.ui.city.data.usecase.GetCityRepositoryUseCaseImpl
import com.freebie.frieebiemobile.ui.city.domain.usecase.GetCityRepositoryUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CityBindingModule {

    @Binds
    abstract fun providerCityApi(impl: CityApiImpl): CityApi

    @Binds
    abstract fun bindCityRepository(impl: CityRepositoryImpl): CityRepository

    @Binds
    abstract fun bindGetCityUseCase(impl: GetCityRepositoryUseCaseImpl): GetCityRepositoryUseCase

}