package com.freebie.frieebiemobile.ui.company.domain.model

import com.freebie.frieebiemobile.ui.account.domain.model.CouponsByGroupModel
import com.freebie.frieebiemobile.ui.feed.domain.BookletByStatus

class CompanyModel(
    val id: String,
    val name: String,
    val avatar: String,
    val description: String,
    val coupons: List<CouponsByGroupModel>,
    val booklets: List<BookletByStatus>
)