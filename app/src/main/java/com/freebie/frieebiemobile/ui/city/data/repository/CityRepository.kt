package com.freebie.frieebiemobile.ui.city.data.repository

import com.freebie.frieebiemobile.ui.category.domain.model.CategoryModel
import com.freebie.frieebiemobile.ui.city.data.api.CityApi
import com.freebie.frieebiemobile.ui.city.domain.model.CityModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CityRepository {
    suspend fun getCityList(): Result<List<CityModel>>
}

class CityRepositoryImpl @Inject constructor(
    private val cityApi: CityApi
) : CityRepository {

    override suspend fun getCityList(): Result<List<CityModel>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = cityApi.requestCities().cities.map {
                CityModel(
                    cityId = it.cityId,
                    cityName = it.cityName
                )
            }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}