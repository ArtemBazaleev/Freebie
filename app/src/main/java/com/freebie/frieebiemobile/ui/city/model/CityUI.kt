package com.freebie.frieebiemobile.ui.city.model


sealed interface CityItem {
    fun getItemViewType(): Int
}

data class CityUI(
    val id: String,
    val name: String
) : CityItem {
    override fun getItemViewType() = CityViewType.CITY.ordinal
}

object ShimmerCity : CityItem {
    override fun getItemViewType() = CityViewType.SHIMMER.ordinal
}


enum class CityViewType {
    CITY, SHIMMER
}