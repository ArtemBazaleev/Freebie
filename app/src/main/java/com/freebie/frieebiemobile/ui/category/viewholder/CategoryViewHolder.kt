package com.freebie.frieebiemobile.ui.category.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.category.model.CategoryItem
import com.freebie.frieebiemobile.ui.category.model.CategoryUI

class CategoryViewHolder(
    itemView: View,
    val clickListener: (CategoryItem) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val groupName: TextView = itemView.findViewById(R.id.group_name)
    private val bgView: CardView = itemView.findViewById(R.id.category_card)
    private val groupImage: ImageView = itemView.findViewById(R.id.group_image)
    private var data: CategoryUI? = null

    init {
        itemView.setOnClickListener {
            data?.let { clickListener.invoke(it) }
        }
    }

    fun bind(data: CategoryUI) {
        this.data = data
        groupName.text = data.name
        bgView.setCardBackgroundColor(data.color)
        Glide.with(bgView.context).load(data.image).into(groupImage)
    }
}