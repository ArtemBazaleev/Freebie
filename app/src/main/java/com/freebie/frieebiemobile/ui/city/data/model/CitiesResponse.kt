package com.freebie.frieebiemobile.ui.city.data.model

data class CitiesResponse(
    val cities: List<CityResponse>
)

data class CityResponse(
    val cityId: String,
    val cityName: String
)