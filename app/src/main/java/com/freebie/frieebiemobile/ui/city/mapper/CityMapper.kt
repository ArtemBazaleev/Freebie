package com.freebie.frieebiemobile.ui.city.mapper

import com.freebie.frieebiemobile.ui.city.domain.model.CityModel
import com.freebie.frieebiemobile.ui.city.model.CityUI
import javax.inject.Inject

class CityMapper @Inject constructor(

) {
    fun mapToUi(model: CityModel): CityUI {
        return CityUI(
            id = model.cityId,
            name = model.cityName
        )
    }
}