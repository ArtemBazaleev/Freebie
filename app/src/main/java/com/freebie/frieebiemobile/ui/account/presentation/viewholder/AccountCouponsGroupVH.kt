package com.freebie.frieebiemobile.ui.account.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.account.presentation.adapter.CouponTitlesGroupAdapter
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupsUIModel

class AccountCouponsGroupVH(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val adapter: CouponTitlesGroupAdapter = CouponTitlesGroupAdapter {  }
    private val rv: RecyclerView = itemView.findViewById(R.id.rv_coupons_types)

    init {
        rv.adapter = adapter
    }

    fun bind(model: CouponGroupsUIModel) {
        adapter.submitList(model.groups)
    }
}