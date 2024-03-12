package com.freebie.frieebiemobile.ui.feed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.models.AddOfferUiModel
import com.freebie.frieebiemobile.ui.feed.models.OfferAdapterUiModel
import com.freebie.frieebiemobile.ui.feed.models.OfferUI
import com.freebie.frieebiemobile.ui.feed.viewholder.AddOfferViewHolder
import com.freebie.frieebiemobile.ui.feed.viewholder.OfferViewHolder

class OffersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<OfferAdapterUiModel>() {
            override fun areItemsTheSame(
                oldItem: OfferAdapterUiModel,
                newItem: OfferAdapterUiModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: OfferAdapterUiModel,
                newItem: OfferAdapterUiModel
            ): Boolean {
                return oldItem == newItem
            }
        })

    fun submitList(list: List<OfferAdapterUiModel>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_offer -> OfferViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_offer, parent, false)
            )

            R.layout.item_add_offer -> AddOfferViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_add_offer, parent, false)
            )

            else -> error("view type is not supported")
        }

    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].layoutId
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OfferViewHolder -> holder.bind(differ.currentList[position] as OfferUI)
            is AddOfferViewHolder -> holder.bind(differ.currentList[position] as AddOfferUiModel)
        }
    }
}