package com.freebie.frieebiemobile.ui.feed.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.models.CouponUI

class CouponViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val couponBgImage: ImageView = itemView.findViewById(R.id.iv_coupon_image)
    private val discountLabel: TextView = itemView.findViewById(R.id.tv_coupon_label)
    private val couponName: TextView = itemView.findViewById(R.id.tv_coupon_name)
    private val couponDescription: TextView = itemView.findViewById(R.id.tv_coupon_description)

    fun bind(data: CouponUI) {
        Glide.with(couponBgImage.context)
            .load(data.avatar)
            .centerCrop()
            .into(couponBgImage)
        discountLabel.text = data.discount
        couponName.text = data.name
        couponDescription.text = data.description
    }
}