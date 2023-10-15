package com.freebie.frieebiemobile.ui.feed.data

import com.freebie.frieebiemobile.network.NoInternetException
import com.freebie.frieebiemobile.ui.feed.domain.EmptyFeed
import com.freebie.frieebiemobile.ui.feed.domain.ErrorWhileLoadingFirstPage
import com.freebie.frieebiemobile.ui.feed.domain.FeedFetcher
import com.freebie.frieebiemobile.ui.feed.domain.FetcherEvents
import com.freebie.frieebiemobile.ui.feed.domain.NoInternet
import com.freebie.frieebiemobile.ui.feed.models.FeedItem
import com.freebie.frieebiemobile.ui.feed.models.FeedShimmer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class FeedFetcherImpl @Inject constructor(
    private val feedRepository: FeedRepository,
    private val companyListMapper: CompanyListMapper
) : FeedFetcher {

    private val _list = MutableStateFlow<List<FeedItem>>(listOf(FeedShimmer, FeedShimmer))
    private val _events = MutableSharedFlow<FetcherEvents>()
    private var category: String? = null
    private val isLoading = AtomicBoolean(false)
    private val hasMore = AtomicBoolean(true)
    private val page = AtomicInteger(0)

    override fun listFlow(): Flow<List<FeedItem>> = _list

    override fun eventsFlow(): Flow<FetcherEvents> = _events

    override suspend fun register(category: String) {
        this.category = category
        loadMore()
    }

    override suspend fun loadMore() = withContext(Dispatchers.IO) {
        if (category.isNullOrEmpty()) {
            throw IllegalStateException("category is empty or null forget to call register with correct params?")
        }
        if (isLoading.get()) return@withContext
        try {
            isLoading.set(true)
            val response = feedRepository.requestFeed(
                page = page.getAndIncrement(),
                size = DEFAULT_PAGE_SIZE,
                category = category!!
            )
            val newList = mutableListOf<FeedItem>().apply {
                if (page.get() > 1) addAll(_list.value)
                addAll(companyListMapper.map(response).apply {
                    if (isNotEmpty()) hasMore.set(true)
                    else hasMore.set(false)
                })
            }
            if (newList.isEmpty() && page.get() == 1) emitEmptyListAndEventIfFirstPage(EmptyFeed)
            else _list.emit(newList)

            isLoading.set(false)
        } catch (e: NoInternetException) {
            Timber.e(e)
            emitEmptyListAndEventIfFirstPage(NoInternet)
            isLoading.set(false)
        } catch (e: Exception) {
            Timber.e(e)
            emitEmptyListAndEventIfFirstPage(ErrorWhileLoadingFirstPage)
            isLoading.set(false)
        }
    }

    private fun isFirstPage() = page.get() == 1

    private suspend fun emitEmptyListAndEventIfFirstPage(event: FetcherEvents) {
        if (isFirstPage()) {
            _list.emit(emptyList())
            _events.emit(event)
        }
    }

    override fun isLoading() = isLoading.get()

    override suspend fun refresh() = withContext(Dispatchers.IO) {
        if (isLoading()) return@withContext
        if (_list.value.isEmpty()) _list.emit(listOf(FeedShimmer, FeedShimmer))
        hasMore.set(true)
        page.set(0)
        loadMore()
    }

    override fun hasMore() = hasMore.get()

    companion object {
        const val DEFAULT_PAGE_SIZE = 15
    }
}