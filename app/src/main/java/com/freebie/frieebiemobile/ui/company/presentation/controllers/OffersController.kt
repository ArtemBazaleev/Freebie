package com.freebie.frieebiemobile.ui.company.presentation.controllers

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyDetailsUiState
import com.freebie.frieebiemobile.ui.feed.adapter.OffersAdapter
import com.freebie.frieebiemobile.ui.feed.models.OfferAdapterUiModel
import com.freebie.frieebiemobile.ui.utils.PaginationCallback
import com.freebie.frieebiemobile.ui.utils.RecyclerPaginationUtil

class OffersController(
    private val offerHeader: TextView,
    private val offerRecycler: RecyclerView,
    private val clickListener: (OfferAdapterUiModel) -> Unit
) {
    private val adapter = OffersAdapter()

    init {
        offerRecycler.adapter = adapter
    }

    fun initPaging(paginationCallback: PaginationCallback) {
        offerRecycler.addOnScrollListener(
            RecyclerPaginationUtil(
                offerRecycler.layoutManager as LinearLayoutManager,
                paginationCallback,
                threshHold = 5
            )
        )
    }

    fun handleState(state: CompanyDetailsUiState) {
        adapter.submitList(state.booklets)
    }
}