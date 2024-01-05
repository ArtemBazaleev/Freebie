package com.freebie.frieebiemobile.ui.rate.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateUiModel

class RateAdapter : RecyclerView.Adapter<RateViewHolder>()  {

    private val differ = AsyncListDiffer(
        this,
        object : DiffUtil.ItemCallback<RateUiModel>() {
            override fun areItemsTheSame(
                oldItem: RateUiModel,
                newItem: RateUiModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RateUiModel,
                newItem: RateUiModel
            ): Boolean {
                return oldItem == newItem
            }
        })

    fun submitList(list: List<RateUiModel>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_rate, parent, false)
        return RateViewHolder(view)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }
}