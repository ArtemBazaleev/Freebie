package com.freebie.frieebiemobile.ui.feed.di

import com.freebie.frieebiemobile.ui.feed.data.FeedApi
import com.freebie.frieebiemobile.ui.feed.data.FeedApiImpl
import com.freebie.frieebiemobile.ui.feed.data.FeedFetcherImpl
import com.freebie.frieebiemobile.ui.feed.data.FeedRepository
import com.freebie.frieebiemobile.ui.feed.data.FeedRepositoryImpl
import com.freebie.frieebiemobile.ui.feed.domain.FeedFetcher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class FeedModule {

    @Binds
    abstract fun provideFeedRepository(impl: FeedRepositoryImpl): FeedRepository

    @Binds
    abstract fun provideFeedApi(impl: FeedApiImpl): FeedApi

    @Binds
    @ViewModelScoped
    abstract fun provideFetcher(impl: FeedFetcherImpl): FeedFetcher

}