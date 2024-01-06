package com.freebie.frieebiemobile.ui.rate.presentation.paging

import android.util.Log
import com.freebie.frieebiemobile.ui.rate.domain.RateModel
import com.freebie.frieebiemobile.ui.rate.domain.usecase.GetReviewForCompanyUseCase
import com.freebie.frieebiemobile.ui.utils.PaginationCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class ReviewPagingHelper @Inject constructor(
    private val getReviewForCompanyUseCase: GetReviewForCompanyUseCase
) : PaginationCallback {

    private var scope: CoroutineScope? = null
    private var listObserver: ((List<RateModel>) -> Unit)? = null
    private var companyId: String? = null
    private var loadMoreJob: Job? = null
    private val isBottomReached = AtomicBoolean(false)

    private val page = AtomicInteger(0)

    fun init(
        scope: CoroutineScope,
        companyId: String,
        newListObserver: (
            List<RateModel>
        ) -> Unit
    ) {
        Log.d("ReviewPagingHelper", "init companyId: $companyId")
        if (this.scope != null) return
        this.scope = scope
        this.listObserver = newListObserver
        this.companyId = companyId
    }

    override fun loadAfter() {
        if (loadMoreJob?.isActive == true) return
        if (companyId == null) error("PagingHelper is not inited")
        loadMoreJob = scope?.launch {
            val response = getReviewForCompanyUseCase.getReviewForCompany(
                companyId!!,
                page.getAndIncrement(),
                20
            )
            response.onSuccess { reviews ->
                Log.d("ReviewPagingHelper", "response.onSuccess ${reviews.size}")
                if (reviews.isEmpty()) isBottomReached.set(true)
                listObserver?.invoke(reviews)
            }
        }
    }

    override fun isLoadingAfter(): Boolean {
        return loadMoreJob?.isActive == true
    }

    override fun isBottomPage(): Boolean {
        return isBottomReached.get()
    }

    fun clear() {
        loadMoreJob?.cancel()
        page.set(0)
        isBottomReached.set(false)
    }
}