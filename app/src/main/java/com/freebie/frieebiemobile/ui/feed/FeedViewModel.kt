package com.freebie.frieebiemobile.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.ui.feed.adapter.FeedClickListener
import com.freebie.frieebiemobile.ui.feed.domain.EmptyFeed
import com.freebie.frieebiemobile.ui.feed.domain.ErrorWhileLoadingFirstPage
import com.freebie.frieebiemobile.ui.feed.domain.FeedFetcher
import com.freebie.frieebiemobile.ui.feed.domain.FetcherEvents
import com.freebie.frieebiemobile.ui.feed.domain.NoInternet
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.feed.models.FeedItem
import com.freebie.frieebiemobile.ui.feed.models.FeedState
import com.freebie.frieebiemobile.ui.feed.models.PlaceHolderInfo
import com.freebie.frieebiemobile.ui.utils.PaginationCallback
import com.freebie.frieebiemobile.ui.utils.PlaceHolderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedFetcher: FeedFetcher
) : ViewModel(), PaginationCallback {
    private val isPlaceHolderAnimationShown = AtomicBoolean(false)
    private val _state = MutableStateFlow(
        FeedState(emptyList(), PlaceHolderInfo(false, null))
    )
    val state: Flow<FeedState> = _state


    init {
        requestFeed()
    }

    private fun requestFeed() {
        viewModelScope.launch {
            feedFetcher.listFlow().collect {
                _state.emit(_state.value.copy(feedList = it))
            }
        }

        viewModelScope.launch {
            feedFetcher.eventsFlow().collect(::handleEvent)
        }

        viewModelScope.launch {
            feedFetcher.register("-1")
        }
    }

    private fun handleEvent(event: FetcherEvents) = viewModelScope.launch {
        when (event) {
            EmptyFeed -> _state.emit(
                _state.value.copy(
                    placeHolderInfo = PlaceHolderInfo(
                        isPlaceHolderAnimationShown.get().not(),
                        PlaceHolderState.NoData
                    )
                ).apply { isPlaceHolderAnimationShown.set(true) }
            )
            ErrorWhileLoadingFirstPage -> _state.emit(
                _state.value.copy(
                    placeHolderInfo = PlaceHolderInfo(
                        isPlaceHolderAnimationShown.get().not(),
                        PlaceHolderState.SomethingWentWrong
                    )
                ).apply { isPlaceHolderAnimationShown.set(true) }
            )

            NoInternet -> _state.emit(
                _state.value.copy(
                    placeHolderInfo = PlaceHolderInfo(
                        isPlaceHolderAnimationShown.get().not(),
                        PlaceHolderState.NoInternet
                    )
                ).apply { isPlaceHolderAnimationShown.set(true) }
            )
        }
    }

    override fun loadAfter() {
        viewModelScope.launch {
            feedFetcher.loadMore()
        }
    }

    override fun isBottomPage(): Boolean {
        return !feedFetcher.hasMore()
    }

    override fun isLoadingAfter(): Boolean {
        return feedFetcher.isLoading()
    }

    fun refresh() {
        viewModelScope.launch {
            val newState = _state.value.copy(
                placeHolderInfo = PlaceHolderInfo(
                    isPlaceHolderAnimationShown.getAndSet(false),
                    null
                )
            )
            _state.emit(newState)
            feedFetcher.refresh()
        }
    }
}