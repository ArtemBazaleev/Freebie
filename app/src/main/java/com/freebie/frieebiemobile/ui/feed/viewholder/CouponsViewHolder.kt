package com.freebie.frieebiemobile.ui.feed.viewholder

import android.view.View
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.adapter.CouponsAdapter
import com.freebie.frieebiemobile.ui.feed.models.CouponsItem


class CouponsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_coupons)
    private val adapter = CouponsAdapter()

    init {
        recyclerView.adapter = adapter
        PagerSnapHelper().apply { attachToRecyclerView(recyclerView) }
    }

    fun bind(data: CouponsItem) {
        adapter.submitList(data.coupons)
    }
}