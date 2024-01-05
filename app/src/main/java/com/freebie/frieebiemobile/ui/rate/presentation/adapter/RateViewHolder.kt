package com.freebie.frieebiemobile.ui.rate.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateUiModel

class RateViewHolder(itemView: View) : ViewHolder(itemView) {
    private val name = itemView.findViewById<TextView>(R.id.reviewer_name)
    private val comment = itemView.findViewById<TextView>(R.id.reviewer_comment)

    fun bind(rateUiModel: RateUiModel) {
        name.text = rateUiModel.reviewerName
        comment.text = rateUiModel.comment
    }
}