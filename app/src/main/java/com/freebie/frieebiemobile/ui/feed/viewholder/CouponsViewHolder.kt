package com.freebie.frieebiemobile.ui.feed.viewholder

import android.view.View
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.adapter.CouponsAdapter
import com.freebie.frieebiemobile.ui.feed.adapter.FeedClickListener
import com.freebie.frieebiemobile.ui.feed.models.CouponsItem


class CouponsViewHolder(
    private val clickListener: FeedClickListener,
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_coupons)
    private val adapter = CouponsAdapter(clickListener::onCouponClicked)

    init {
        recyclerView.adapter = adapter
        PagerSnapHelper().apply { attachToRecyclerView(recyclerView) }
    }

    fun bind(data: CouponsItem) {
        adapter.submitList(data.coupons)
    }
}