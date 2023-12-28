package com.freebie.frieebiemobile.ui.company.presentation.paging

import android.util.Log
import com.freebie.frieebiemobile.ui.account.presentation.mappers.CouponUiMapper
import com.freebie.frieebiemobile.ui.coupon.domain.GetCouponsByCompanyUseCase
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.utils.PaginationCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class CouponsCompanyPagingHelper @Inject constructor(
    private val getCouponsByCompanyUseCase: GetCouponsByCompanyUseCase,
    private val mapper: CouponUiMapper
) : PaginationCallback {

    private var scope: CoroutineScope? = null
    private var listObserver: ((List<CouponUI>) -> Unit)? = null
    private var companyId: String? = null
    private var loadMoreJob: Job? = null
    private val isBottomReached = AtomicBoolean(false)

    private val page = AtomicInteger(0)

    fun init(
        scope: CoroutineScope,
        companyId: String,
        newListObserver: (
            List<CouponUI>
        ) -> Unit
    ) {
        Log.d("CouponsCompanyPagingHelper", "init companyId: $companyId")
        if (this.scope != null) return
        this.scope = scope
        this.listObserver = newListObserver
        this.companyId = companyId
    }

    override fun loadAfter() {
        if (loadMoreJob?.isActive == true) return
        if (companyId == null) error("PagingHelper is not inited")
        loadMoreJob = scope?.launch {
            val response = getCouponsByCompanyUseCase.getCouponsByCompany(companyId!!, page.incrementAndGet(), 10)
            response.onSuccess { coupons ->
                Log.d("CouponsCompanyPagingHelper", "response.onSuccess ${coupons.size}")
                if (coupons.isEmpty()) isBottomReached.set(true)
                listObserver?.invoke(coupons.map(mapper::mapCouponDescription))
            }
        }
    }

    fun clear() {
        loadMoreJob?.cancel()
        page.set(0)
        isBottomReached.set(false)
    }

    override fun isLoadingAfter(): Boolean {
        return loadMoreJob?.isActive == true
    }

    override fun isBottomPage(): Boolean {
        return isBottomReached.get()
    }

}