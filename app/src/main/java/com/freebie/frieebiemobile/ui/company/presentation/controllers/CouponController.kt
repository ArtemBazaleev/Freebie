package com.freebie.frieebiemobile.ui.company.presentation.controllers

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyDetailsUiState
import com.freebie.frieebiemobile.ui.feed.adapter.CouponsAdapter
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.utils.PaginationCallback
import com.freebie.frieebiemobile.ui.utils.RecyclerPaginationUtil

class CouponController(
    private val couponHeader: TextView,
    private val couponRecycler: RecyclerView,
    private val clickListener: (CouponUI) -> Unit
) {
    private val adapter = CouponsAdapter(clickListener)

    init {
        couponRecycler.adapter = adapter
    }

    fun initPaging(paginationCallback: PaginationCallback) {
        couponRecycler.addOnScrollListener(
            RecyclerPaginationUtil(
                couponRecycler.layoutManager as LinearLayoutManager,
                paginationCallback,
                threshHold = 5
            )
        )
    }

    fun handleState(state: CompanyDetailsUiState) {
        adapter.submitList(state.coupons)
    }
}