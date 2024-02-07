package com.freebie.frieebiemobile.ui.rate.data.di

import com.freebie.frieebiemobile.ui.rate.data.api.ReviewApi
import com.freebie.frieebiemobile.ui.rate.data.api.ReviewApiImpl
import com.freebie.frieebiemobile.ui.rate.data.repository.ReviewRepository
import com.freebie.frieebiemobile.ui.rate.data.repository.ReviewRepositoryImpl
import com.freebie.frieebiemobile.ui.rate.data.usecase.GetReviewForCompanyUseCaseImpl
import com.freebie.frieebiemobile.ui.rate.data.usecase.RateCompanyUseCaseImpl
import com.freebie.frieebiemobile.ui.rate.data.usecase.ReplyCommentUseCaseImpl
import com.freebie.frieebiemobile.ui.rate.domain.usecase.GetReviewForCompanyUseCase
import com.freebie.frieebiemobile.ui.rate.domain.usecase.RateCompanyUseCase
import com.freebie.frieebiemobile.ui.rate.domain.usecase.ReplyCommentUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RateModule {
    @Binds
    fun bindApi(impl: ReviewApiImpl): ReviewApi

    @Binds
    fun bindRepo(impl: ReviewRepositoryImpl): ReviewRepository

    @Binds
    fun bindRateCompanyUseCase(impl: RateCompanyUseCaseImpl): RateCompanyUseCase

    @Binds
    fun bindCompanyReviewsListUseCase(impl: GetReviewForCompanyUseCaseImpl) : GetReviewForCompanyUseCase

    @Binds
    fun bindReplyCommentUseCase(impl: ReplyCommentUseCaseImpl): ReplyCommentUseCase
}