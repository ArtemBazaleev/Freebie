package com.freebie.frieebiemobile.ui.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class RecyclerPaginationUtil(
    private val layoutManager: LinearLayoutManager,
    private val pagingCallback: PaginationCallback
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount

        val firstItemVisiblePosition = layoutManager.findFirstVisibleItemPosition()

        if (!pagingCallback.isLoadingAfter() && !pagingCallback.isBottomPage()) {
            if ((visibleItemCount + firstItemVisiblePosition) >= totalItemCount - DISTANCE
                && firstItemVisiblePosition >= 0
            ) {
                recyclerView.post { pagingCallback.loadAfter() }
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val firstItemVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        val delta = recyclerView.bottom - recyclerView.height + recyclerView.scrollY
        if (delta == 0) {
            if (!pagingCallback.isLoadingBefore() && !pagingCallback.isTopPage()) {
                if (firstItemVisiblePosition in 0..DISTANCE) {
                    recyclerView.post { pagingCallback.loadBefore() }
                }
            }
        }
    }
}

private const val DISTANCE = 10

interface PaginationCallback {
    fun loadBefore() = {}
    fun loadAfter()
    fun isTopPage(): Boolean { return true }
    fun isBottomPage(): Boolean { return true }
    fun isLoadingAfter(): Boolean
    fun isLoadingBefore(): Boolean { return true }
}
