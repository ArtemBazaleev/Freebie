package com.freebie.frieebiemobile.ui.feed.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.freebie.frieebiemobile.R

class FeedShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val shimmer = itemView.findViewById<ShimmerFrameLayout>(R.id.feed_shimmer)

    init {
        shimmer.startShimmer()
    }
}