package com.freebie.frieebiemobile.storage

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface StorageModule {
    @Binds
    fun bindSecured(impl: SecuredKeyValueStorageImpl): SecuredKeyValueStorage

    @Binds
    fun bindKeyValuesStorage(impl: KeyValueStorageImpl): KeyValueStorage
}