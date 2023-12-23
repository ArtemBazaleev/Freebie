package com.freebie.frieebiemobile.ui.company.data.di

import com.freebie.frieebiemobile.ui.company.data.api.CompanyApi
import com.freebie.frieebiemobile.ui.company.data.api.CompanyApiImpl
import com.freebie.frieebiemobile.ui.company.data.repository.CompanyRepository
import com.freebie.frieebiemobile.ui.company.data.repository.CompanyRepositoryImpl
import com.freebie.frieebiemobile.ui.company.data.usecase.GetCompanyDetailsUseCaseImpl
import com.freebie.frieebiemobile.ui.company.domain.usecase.GetCompanyDetailsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface CompanyDataModule {

    @Binds
    fun bindApi(impl: CompanyApiImpl): CompanyApi

    @Binds
    fun bindRepo(impl: CompanyRepositoryImpl): CompanyRepository

    @Binds
    fun bindGetCompanyDetails(impl: GetCompanyDetailsUseCaseImpl): GetCompanyDetailsUseCase
}