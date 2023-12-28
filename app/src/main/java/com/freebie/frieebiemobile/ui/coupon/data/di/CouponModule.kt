package com.freebie.frieebiemobile.ui.coupon.data.di

import com.freebie.frieebiemobile.ui.coupon.data.api.CouponApi
import com.freebie.frieebiemobile.ui.coupon.data.api.CouponApiImpl
import com.freebie.frieebiemobile.ui.coupon.data.repository.CouponRepository
import com.freebie.frieebiemobile.ui.coupon.data.repository.CouponRepositoryImpl
import com.freebie.frieebiemobile.ui.coupon.data.usecase.GetCouponDetailsUseCaseImpl
import com.freebie.frieebiemobile.ui.coupon.data.usecase.GetCouponsByCompanyUseCaseImpl
import com.freebie.frieebiemobile.ui.coupon.domain.GetCouponDetailsUseCase
import com.freebie.frieebiemobile.ui.coupon.domain.GetCouponsByCompanyUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface CouponModule {

    @Binds
    fun bindRepo(impl: CouponRepositoryImpl): CouponRepository

    @Binds
    fun bindApi(impl: CouponApiImpl): CouponApi

    @Binds
    fun getCouponDetails(impl: GetCouponDetailsUseCaseImpl): GetCouponDetailsUseCase

    @Binds
    fun bindCouponsByCompany(impl: GetCouponsByCompanyUseCaseImpl): GetCouponsByCompanyUseCase
}