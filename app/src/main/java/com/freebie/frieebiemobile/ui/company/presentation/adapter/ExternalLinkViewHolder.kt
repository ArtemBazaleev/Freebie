package com.freebie.frieebiemobile.ui.company.presentation.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.company.presentation.model.ExternalLinkUiModel

class ExternalLinkViewHolder(
    private val clickListener: (ExternalLinkUiModel) -> Unit = {},
    itemView: View,
): ViewHolder(itemView) {

    private var model: ExternalLinkUiModel? = null
    private val icon: ImageView = itemView.findViewById(R.id.external_icon)
    private val name: TextView = itemView.findViewById(R.id.external_name)

    init {
        itemView.setOnClickListener {
            model?.let(clickListener)
        }
    }


    fun bind(model: ExternalLinkUiModel) {
        this.model = model
        Glide.with(itemView.context)
            .load(model.linkType.iconRes)
            .into(icon)
        name.setText(model.linkType.textRes)
    }

}