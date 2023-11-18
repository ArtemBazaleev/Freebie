package com.freebie.frieebiemobile.ui.account.data.di

import com.freebie.frieebiemobile.ui.account.data.api.AccountApi
import com.freebie.frieebiemobile.ui.account.data.api.AccountApiImpl
import com.freebie.frieebiemobile.ui.account.data.db.UserProfileDaoProvider
import com.freebie.frieebiemobile.ui.account.data.db.UserProfileDaoProviderImpl
import com.freebie.frieebiemobile.ui.account.data.repository.AccountRepository
import com.freebie.frieebiemobile.ui.account.data.repository.AccountRepositoryImpl
import com.freebie.frieebiemobile.ui.account.data.repository.UserProfileRepository
import com.freebie.frieebiemobile.ui.account.data.repository.UserProfileRepositoryImpl
import com.freebie.frieebiemobile.ui.account.data.storage.UserProfileStorage
import com.freebie.frieebiemobile.ui.account.data.storage.UserProfileStorageImpl
import com.freebie.frieebiemobile.ui.account.data.usecase.GetAccountInfoUseCaseImpl
import com.freebie.frieebiemobile.ui.account.data.usecase.OwnUserProfileUseCaseImpl
import com.freebie.frieebiemobile.ui.account.domain.GetAccountInfoUseCase
import com.freebie.frieebiemobile.ui.account.domain.OwnUserProfileUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
interface AccountDataBind {

    @Binds
    fun bindStorage(impl: UserProfileStorageImpl): UserProfileStorage

    @Binds
    fun bindRepository(impl: UserProfileRepositoryImpl): UserProfileRepository

    @Binds
    fun bindOwnProfile(impl: OwnUserProfileUseCaseImpl): OwnUserProfileUseCase

    @Binds
    fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    fun bindGetAccountUseCase(impl: GetAccountInfoUseCaseImpl): GetAccountInfoUseCase

    @Binds
    fun bindAccountApi(impl: AccountApiImpl): AccountApi

}

@Module
@InstallIn(SingletonComponent::class)
interface AccountDBModule {
    @Binds
    @Singleton
    fun bindDao(impl: UserProfileDaoProviderImpl): UserProfileDaoProvider
}