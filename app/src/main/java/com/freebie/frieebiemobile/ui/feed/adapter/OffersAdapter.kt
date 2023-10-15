package com.freebie.frieebiemobile.ui.feed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.models.OfferUI
import com.freebie.frieebiemobile.ui.feed.viewholder.OfferViewHolder

class OffersAdapter : RecyclerView.Adapter<OfferViewHolder>() {

    private val mCurrentList: MutableList<OfferUI> = mutableListOf()

    fun submitList(list: List<OfferUI>) {
        mCurrentList.clear()
        mCurrentList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        return OfferViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_offer, parent, false)
        )
    }

    override fun getItemCount() = mCurrentList.size

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.bind(mCurrentList[position])
    }
}