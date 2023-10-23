package com.freebie.frieebiemobile.storage

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface StorageModule {
    @Binds
    fun bindSecured(impl: SecuredKeyValueStorageImpl): SecuredKeyValueStorage
}