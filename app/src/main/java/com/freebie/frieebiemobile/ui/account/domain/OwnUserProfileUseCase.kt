package com.freebie.frieebiemobile.ui.account.domain

import kotlinx.coroutines.flow.Flow

interface OwnUserProfileUseCase {
    fun getOwnUserProfileFlow(): Flow<ProfileModel>
}