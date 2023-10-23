package com.freebie.frieebiemobile.login

import com.freebie.frieebiemobile.login.data.api.AuthApi
import com.freebie.frieebiemobile.login.data.api.AuthApiImpl
import com.freebie.frieebiemobile.login.data.repository.AuthRepository
import com.freebie.frieebiemobile.login.data.repository.AuthRepositoryImpl
import com.freebie.frieebiemobile.login.domain.AuthGoogleUseCase
import com.freebie.frieebiemobile.login.domain.AuthGoogleUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
interface BindVmModule {

    @Binds
    fun bindRepo(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindsStorage(impl: TokenStorageImpl): TokenStorage

    @Binds
    fun bindApi(impl: AuthApiImpl): AuthApi

    @Binds
    fun bindAuthUseCase(impl: AuthGoogleUseCaseImpl): AuthGoogleUseCase

}

@Module
@InstallIn(SingletonComponent::class)
interface BindSingletonModule {
    @Binds
    fun bindGoogleAuth(impl: GoogleAuthImpl): GoogleAuth
}
