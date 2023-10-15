package com.freebie.frieebiemobile.ui.feed.domain

import com.freebie.frieebiemobile.ui.feed.models.FeedItem
import kotlinx.coroutines.flow.Flow

interface FeedFetcher {
    fun listFlow(): Flow<List<FeedItem>>
    suspend fun register(category: String)
    suspend fun loadMore()
    fun isLoading(): Boolean
    suspend fun refresh()
    fun hasMore(): Boolean
    fun eventsFlow(): Flow<FetcherEvents>
}

sealed interface FetcherEvents

object ErrorWhileLoadingFirstPage: FetcherEvents
object EmptyFeed: FetcherEvents
object NoInternet: FetcherEvents