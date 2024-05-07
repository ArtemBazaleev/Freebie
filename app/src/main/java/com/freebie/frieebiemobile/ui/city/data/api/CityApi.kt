package com.freebie.frieebiemobile.ui.city.data.api

import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.frieebiemobile.ui.city.data.model.CitiesResponse
import com.freebie.frieebiemobile.ui.city.data.model.CityResponse
import com.freebie.protos.CategoryApiProtos.CategoryListRequest
import com.freebie.protos.CommonApiProtos
import javax.inject.Inject

interface CityApi {
    suspend fun requestCities(): CitiesResponse
}

class CityApiImpl @Inject constructor(
    private val httpAccess: HttpAccess
): CityApi {

    override suspend fun requestCities(): CitiesResponse {
        val response = httpAccess.httpRequest(
            requestUrlSegment = CITY_LIST_API,
            headers = emptyMap(),
            method = Method.GET
        )
        val proto = CommonApiProtos.SupportedCityResponse.parseFrom(response.bodyAsArray)
        return CitiesResponse(
            cities = proto.supportedCitiesList.map { protoCity ->
                CityResponse(
                    cityId = protoCity.encryptedId,
                    cityName = protoCity.name
                )
            }
        )
    }

    companion object {
        private const val CITY_LIST_API = "v1/common/cities"
    }

}