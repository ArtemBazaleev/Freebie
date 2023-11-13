package com.freebie.frieebiemobile.ui.account.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupUiModel
import com.freebie.frieebiemobile.ui.account.presentation.viewholder.CouponTitleViewHolder

class CouponTitlesGroupAdapter(
    val titleClickListener: (CouponGroupUiModel) -> Unit
) : RecyclerView.Adapter<CouponTitleViewHolder>() {

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<CouponGroupUiModel>() {
        override fun areItemsTheSame(oldItem: CouponGroupUiModel, newItem: CouponGroupUiModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CouponGroupUiModel, newItem: CouponGroupUiModel): Boolean {
            return oldItem == newItem
        }

    })

    fun submitList(list: List<CouponGroupUiModel>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponTitleViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_coupon_type, parent, false)
        return CouponTitleViewHolder(view, titleClickListener)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: CouponTitleViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }
}