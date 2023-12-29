package com.freebie.frieebiemobile.ui.company.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.company.presentation.model.ExternalLinkUiModel

class ExternalLinkAdapter(
    private val clickListener: (ExternalLinkUiModel) -> Unit
) : RecyclerView.Adapter<ExternalLinkViewHolder>() {

    private val differ = AsyncListDiffer(
        this,
        object : DiffUtil.ItemCallback<ExternalLinkUiModel>() {
            override fun areItemsTheSame(
                oldItem: ExternalLinkUiModel,
                newItem: ExternalLinkUiModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ExternalLinkUiModel,
                newItem: ExternalLinkUiModel
            ): Boolean {
                return oldItem == newItem
            }
        })

    fun submitList(list: List<ExternalLinkUiModel>) = differ.submitList(list)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExternalLinkViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_external_link, parent, false)
        return ExternalLinkViewHolder(clickListener, view)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ExternalLinkViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }


}