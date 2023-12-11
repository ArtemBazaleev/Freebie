package com.freebie.frieebiemobile.ui.account.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.account.presentation.adapter.AccountClickListener
import com.freebie.frieebiemobile.ui.account.presentation.adapter.CouponTitlesGroupAdapter
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupsUIModel

class AccountCouponsGroupVH(
    itemView: View,
    private val accountClickListener: AccountClickListener
) : RecyclerView.ViewHolder(itemView) {

    private val adapter: CouponTitlesGroupAdapter = CouponTitlesGroupAdapter(accountClickListener::couponGroupClick)
    private val rv: RecyclerView = itemView.findViewById(R.id.rv_coupons_types)

    init {
        rv.adapter = adapter
        rv.itemAnimator = null
    }

    fun bind(model: CouponGroupsUIModel) {
        adapter.submitList(model.groups)
    }
}