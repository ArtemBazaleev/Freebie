package com.freebie.frieebiemobile.ui.account.presentation.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountCompanyUIModel

class AccountCompanyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val companyNameTexView = itemView.findViewById<TextView>(R.id.company_name)
    private val companyAvatar = itemView.findViewById<ImageView>(R.id.company_avatar)

    fun bind(model: AccountCompanyUIModel) {
        companyNameTexView.text = model.companyName
        Glide.with(itemView.context)
            .load(model.companyAvatar)
            .circleCrop()
            .into(companyAvatar)
    }

}