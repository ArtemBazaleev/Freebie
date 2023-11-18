package com.freebie.frieebiemobile.ui.account.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountCouponsUIModel
import com.freebie.frieebiemobile.ui.feed.adapter.CouponsAdapter

class AccountCouponsVH(val itemView: View): RecyclerView.ViewHolder(itemView) {

    private val adapter = CouponsAdapter()
    private val rvCoupons = itemView.findViewById<RecyclerView>(R.id.rv_coupons)

    init {
        rvCoupons.adapter = adapter
    }

    fun bind(model: AccountCouponsUIModel) {
        adapter.submitList(model.coupons)
    }
}