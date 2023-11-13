package com.freebie.frieebiemobile.ui.account.presentation.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupUiModel

class CouponTitleViewHolder(itemView: View, titleClickListener: (CouponGroupUiModel) -> Unit) : ViewHolder(itemView) {

    private val title by lazy { itemView.findViewById<AppCompatTextView>(R.id.title) }
    private val cardView by lazy { itemView.findViewById<CardView>(R.id.cv_coupon_title) }
    private var model: CouponGroupUiModel? = null

    init {
        itemView.setOnClickListener {
            model?.let(titleClickListener)
        }
    }

    fun bind(model: CouponGroupUiModel) {
        this.model = model
        title.setText(model.groupTitle)
        if (model.isActive) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.selected_card_bg))
        } else {
            cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.regular_card_bg))
        }
    }

}