package com.freebie.frieebiemobile.ui.feed.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.models.OfferUI

class OfferViewHolder(itemView: View) : ViewHolder(itemView) {
    private val offerImage: ImageView = itemView.findViewById(R.id.iv_offer)
    fun bind(data: OfferUI) {
        Glide.with(offerImage.context)
            .load(data.avatar)
            .centerCrop()
            .into(offerImage)
    }
}