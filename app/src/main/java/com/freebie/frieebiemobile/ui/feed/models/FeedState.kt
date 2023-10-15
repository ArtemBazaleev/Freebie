package com.freebie.frieebiemobile.ui.feed.models

import com.freebie.frieebiemobile.ui.utils.PlaceHolderState

data class FeedState (
    val feedList: List<FeedItem>,
    val placeHolderInfo: PlaceHolderInfo
)

data class PlaceHolderInfo(
    val needToAnimate: Boolean,
    val state: PlaceHolderState?
)