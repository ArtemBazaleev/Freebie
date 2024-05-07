package com.freebie.frieebiemobile.ui.city.data.usecase

import com.freebie.frieebiemobile.ui.city.data.repository.CityRepository
import com.freebie.frieebiemobile.ui.city.domain.model.CityModel
import com.freebie.frieebiemobile.ui.city.domain.usecase.GetCityRepositoryUseCase
import javax.inject.Inject

class GetCityRepositoryUseCaseImpl @Inject constructor(
    private val repository: CityRepository
): GetCityRepositoryUseCase {
    override suspend fun invoke(): Result<List<CityModel>> = repository.getCityList()
}