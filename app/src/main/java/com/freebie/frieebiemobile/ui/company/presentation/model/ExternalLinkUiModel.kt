package com.freebie.frieebiemobile.ui.company.presentation.model
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.freebie.frieebiemobile.R

data class ExternalLinkUiModel(
    val url: String,
    val linkType: LinkUiType
)

enum class LinkUiType(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int
) {
    WhatsApp(
        R.drawable.ic_whatsapp,
        R.string.whatsapp
    ),
    Telegram(
        R.drawable.ic_telegram,
        R.string.telegram
    ),
    Instagram(
        R.drawable.ic_insta,
        R.string.instagram
    )
}