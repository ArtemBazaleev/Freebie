package com.freebie.frieebiemobile.ui.city.domain.usecase

import com.freebie.frieebiemobile.ui.city.domain.model.CityModel

interface GetCityRepositoryUseCase {
    suspend operator fun invoke(): Result<List<CityModel>>
}