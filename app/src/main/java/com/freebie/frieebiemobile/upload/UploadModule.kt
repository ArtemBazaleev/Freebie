package com.freebie.frieebiemobile.upload

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UploadModule {

    @Binds
    fun bindRepo(impl: UploadRepositoryImpl): UploadRepository

}