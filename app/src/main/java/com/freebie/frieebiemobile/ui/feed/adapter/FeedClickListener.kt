package com.freebie.frieebiemobile.ui.feed.adapter

import com.freebie.frieebiemobile.ui.feed.models.CouponUI

interface FeedClickListener {
    fun onCouponClicked(couponUI: CouponUI)
}