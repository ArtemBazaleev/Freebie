package com.freebie.frieebiemobile.ui.account.presentation.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.account.presentation.adapter.AccountClickListener
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountActionButtonUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountHeaderUIModel

class AccountButtonVH(
    val itemView: View,
    private val clickListener: AccountClickListener
) : RecyclerView.ViewHolder(itemView) {

    private val button = itemView.findViewById<AppCompatButton>(R.id.account_action_button)
    private var model: AccountActionButtonUIModel? = null

    init {
        button.setOnClickListener {
            model?.let(clickListener::actionButtonClick)
        }
    }

    fun bind(model: AccountActionButtonUIModel) {
        this.model = model
        button.text = model.text
        val drawableStart = if (model.drawableStart == null) null
        else ContextCompat.getDrawable(itemView.context, model.drawableStart)
        button.setCompoundDrawablesWithIntrinsicBounds(drawableStart, null, null, null)
    }
}