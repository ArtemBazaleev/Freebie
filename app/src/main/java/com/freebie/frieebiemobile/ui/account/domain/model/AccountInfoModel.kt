package com.freebie.frieebiemobile.ui.account.domain.model

class AccountInfoModel(
    val balance: Double,
    val companies: List<CompaniesByGroupModel>,
    val coupons: List<CouponsByGroupModel>
)