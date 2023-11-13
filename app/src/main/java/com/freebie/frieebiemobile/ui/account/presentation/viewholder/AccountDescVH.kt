package com.freebie.frieebiemobile.ui.account.presentation.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountDescUIModel

class AccountDescVH(val itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val descTextView = itemView.findViewById<TextView>(R.id.accont_placeholder)

    fun bind(model: AccountDescUIModel) {
        descTextView.text = model.text
    }
}