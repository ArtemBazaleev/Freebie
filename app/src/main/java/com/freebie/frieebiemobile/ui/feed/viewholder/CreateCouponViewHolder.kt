package com.freebie.frieebiemobile.ui.feed.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.models.CreateCoupon

class CreateCouponViewHolder(
    private val parent: View
) : RecyclerView.ViewHolder(parent) {
    private val image = itemView.findViewById<ImageView>(R.id.iv_coupon_image)

    fun bind(data: CreateCoupon) {
        Glide.with(image).load(data.image).into(image)
    }

}