package com.freebie.frieebiemobile.ui.rate.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateCompanyResponseUIModel

class CompanyReviewResponseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val name = itemView.findViewById<TextView>(R.id.reviewer_name)
    private val comment = itemView.findViewById<TextView>(R.id.reviewer_comment)
    private val date = itemView.findViewById<TextView>(R.id.review_date)


    fun bind(rateCompanyResponseUIModel: RateCompanyResponseUIModel) {
        name.text = name.context.getString(R.string.company_response)
        comment.text = rateCompanyResponseUIModel.comment
        date.text = rateCompanyResponseUIModel.date

    }
}