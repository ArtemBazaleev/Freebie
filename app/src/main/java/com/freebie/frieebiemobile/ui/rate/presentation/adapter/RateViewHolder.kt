package com.freebie.frieebiemobile.ui.rate.presentation.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.rate.presentation.model.UserRateUiModel
import com.freebie.frieebiemobile.ui.utils.gone
import com.freebie.frieebiemobile.ui.utils.visible

class RateViewHolder(itemView: View) : ViewHolder(itemView) {
    private val name = itemView.findViewById<TextView>(R.id.reviewer_name)
    private val comment = itemView.findViewById<TextView>(R.id.reviewer_comment)
    private val review = itemView.findViewById<TextView>(R.id.review_score)
    private val avatar = itemView.findViewById<ImageView>(R.id.user_avatar)
    private val date = itemView.findViewById<TextView>(R.id.review_date)

    fun bind(userRateUiModel: UserRateUiModel) {
        name.text = userRateUiModel.reviewerName
        comment.text = userRateUiModel.comment
        if (userRateUiModel.needToShowFullText) {
            comment.maxLines = Int.MAX_VALUE
        } else {
            comment.maxLines = 3
            comment.setLines(3)
        }

        if (userRateUiModel.needToShowRating && userRateUiModel.reviewerRating != null) {
            review.apply {
                visible()
                text = userRateUiModel.reviewerRating.toString()
            }
        } else review.gone()

        Glide.with(itemView.context)
            .load(userRateUiModel.avatar)
            .circleCrop()
            .into(avatar)

        date.text = userRateUiModel.date
    }
}