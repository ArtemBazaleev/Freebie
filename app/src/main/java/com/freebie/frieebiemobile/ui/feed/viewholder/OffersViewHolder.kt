package com.freebie.frieebiemobile.ui.feed.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.adapter.OffersAdapter
import com.freebie.frieebiemobile.ui.feed.models.OffersItem

class OffersViewHolder(itemView: View) : ViewHolder(itemView) {
    private val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_offers)
    private val adapter = OffersAdapter()

    init {
        recyclerView.adapter = adapter
//        recyclerView.addItemDecoration(OffsetDecorator())
    }

    fun bind(data: OffersItem) {
        adapter.submitList(data.offers)
    }
}