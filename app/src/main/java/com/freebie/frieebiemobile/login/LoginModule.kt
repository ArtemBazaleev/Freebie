package com.freebie.frieebiemobile.login

import com.freebie.frieebiemobile.login.data.api.AuthApi
import com.freebie.frieebiemobile.login.data.api.AuthApiImpl
import com.freebie.frieebiemobile.login.data.repository.AuthRepository
import com.freebie.frieebiemobile.login.data.repository.AuthRepositoryImpl
import com.freebie.frieebiemobile.login.domain.AuthGoogleUseCase
import com.freebie.frieebiemobile.login.domain.AuthGoogleUseCaseImpl
import com.freebie.frieebiemobile.login.domain.AuthStatusUseCase
import com.freebie.frieebiemobile.login.domain.AuthStatusUseCaseImpl
import com.freebie.frieebiemobile.login.domain.LogoutUseCase
import com.freebie.frieebiemobile.login.domain.LogoutUseCaseImpl
import com.freebie.frieebiemobile.login.domain.RegisterFcmUseCase
import com.freebie.frieebiemobile.login.domain.RegisterFcmUseCaseImpl
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
    fun bindApi(impl: AuthApiImpl): AuthApi

    @Binds
    fun bindAuthUseCase(impl: AuthGoogleUseCaseImpl): AuthGoogleUseCase

    @Binds
    fun bindAuthStatusUseCase(impl: AuthStatusUseCaseImpl): AuthStatusUseCase

    @Binds
    fun bindLogoutUseCase(impl: LogoutUseCaseImpl): LogoutUseCase

    @Binds
    fun bindsRegisterFcm(impl: RegisterFcmUseCaseImpl): RegisterFcmUseCase
}

@Module
@InstallIn(SingletonComponent::class)
interface BindSingletonModule {
    @Binds
    fun bindGoogleAuth(impl: GoogleAuthImpl): GoogleAuth

    @Binds
    fun bindsStorage(impl: TokenStorageImpl): TokenStorage
}
