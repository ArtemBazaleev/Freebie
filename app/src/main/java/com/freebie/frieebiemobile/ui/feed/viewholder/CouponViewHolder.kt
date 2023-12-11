package com.freebie.frieebiemobile.ui.feed.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.adapter.FeedClickListener
import com.freebie.frieebiemobile.ui.feed.models.CouponUI

class CouponViewHolder(
    private val clickListener: (CouponUI) -> Unit,
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val couponBgImage: ImageView = itemView.findViewById(R.id.iv_coupon_image)
    private val discountLabel: TextView = itemView.findViewById(R.id.tv_coupon_label)
    private val couponName: TextView = itemView.findViewById(R.id.tv_coupon_name)
    private val couponDescription: TextView = itemView.findViewById(R.id.tv_coupon_description)
    private var data: CouponUI? = null

    init {
        itemView.setOnClickListener {
            data?.let(clickListener)
        }
    }

    fun bind(data: CouponUI) {
        this.data = data
        Glide.with(couponBgImage.context)
            .load(data.avatar)
            .centerCrop()
            .into(couponBgImage)
        discountLabel.text = data.discount
        couponName.text = data.name
        couponDescription.text = data.description
    }
}