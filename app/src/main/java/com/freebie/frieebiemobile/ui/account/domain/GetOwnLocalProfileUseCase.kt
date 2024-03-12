package com.freebie.frieebiemobile.ui.account.domain

interface GetOwnLocalProfileUseCase {
    suspend fun getOwnLocalProfile(): ProfileModel?
}