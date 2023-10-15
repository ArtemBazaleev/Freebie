package com.freebie.frieebiemobile.ui.feed.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.models.CompanyHeaderItem

class CompanyHeaderViewHolder(itemView: View) : ViewHolder(itemView) {

    private val avatar: ImageView = itemView.findViewById(R.id.company_avatar)
    private val companyName: TextView = itemView.findViewById(R.id.company_name)

    fun onBind(item: CompanyHeaderItem) {
        Glide.with(avatar.context)
            .load(item.avatar)
            .circleCrop()
            .into(avatar)
        companyName.text = item.companyName
    }

}