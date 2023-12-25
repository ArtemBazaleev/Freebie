package com.freebie.frieebiemobile.ui.account.presentation.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.account.presentation.adapter.AccountClickListener
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountCompanyUIModel

class AccountCompanyVH(
    itemView: View,
    clickListener: AccountClickListener
) : RecyclerView.ViewHolder(itemView) {

    private val companyNameTexView = itemView.findViewById<TextView>(R.id.company_name)
    private val companyAvatar = itemView.findViewById<ImageView>(R.id.company_avatar)
    private var model: AccountCompanyUIModel? = null

    init {
        itemView.setOnClickListener {
            model?.let(clickListener::companyClicked)
        }
    }

    fun bind(model: AccountCompanyUIModel) {
        this.model = model
        companyNameTexView.text = model.companyName
        Glide.with(itemView.context)
            .load(model.companyAvatar)
            .circleCrop()
            .into(companyAvatar)
    }

}