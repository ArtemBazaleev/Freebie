package com.freebie.frieebiemobile.ui.account.presentation.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountHeaderUIModel

class AccountHeaderVH(val itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val headerTextView = itemView.findViewById<TextView>(R.id.account_header)

    fun bind(model: AccountHeaderUIModel) {
        headerTextView.text = model.text
    }
}