package com.freebie.frieebiemobile.ui.feed.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.models.CompanyHeaderItem

class CompanyHeaderViewHolder(
    private val companyClickListener: (CompanyHeaderItem) -> Unit = {},
    itemView: View
) : ViewHolder(itemView) {

    private val avatar: ImageView = itemView.findViewById(R.id.company_avatar)
    private val companyName: TextView = itemView.findViewById(R.id.company_name)
    private var data: CompanyHeaderItem? = null

    init {
        itemView.setOnClickListener {
            data?.let(companyClickListener)
        }
    }

    fun onBind(item: CompanyHeaderItem) {
        data = item
        Glide.with(avatar.context)
            .load(item.avatar)
            .circleCrop()
            .into(avatar)
        companyName.text = item.companyName
    }

}