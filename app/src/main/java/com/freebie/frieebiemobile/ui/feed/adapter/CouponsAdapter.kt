package com.freebie.frieebiemobile.ui.feed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.models.CouponAdapterUiModel
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.feed.models.CreateCoupon
import com.freebie.frieebiemobile.ui.feed.viewholder.CouponViewHolder
import com.freebie.frieebiemobile.ui.feed.viewholder.CreateCouponViewHolder

class CouponsAdapter(
    private val clickListener: (CouponUI) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, object :
        DiffUtil.ItemCallback<CouponAdapterUiModel>() {
        override fun areItemsTheSame(
            oldItem: CouponAdapterUiModel,
            newItem: CouponAdapterUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CouponAdapterUiModel,
            newItem: CouponAdapterUiModel
        ): Boolean {
            return oldItem == newItem
        }
    })

    fun submitList(list: List<CouponAdapterUiModel>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.item_coupon -> CouponViewHolder(
                clickListener,
                LayoutInflater.from(parent.context).inflate(R.layout.item_coupon, parent, false)
            )
            R.layout.item_add_coupon -> CreateCouponViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_add_coupon, parent, false)
            )
            else -> error("view type is not supported")
        }
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is CouponViewHolder -> holder.bind(differ.currentList[position] as CouponUI)
            is CreateCouponViewHolder -> holder.bind(differ.currentList[position] as CreateCoupon)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].layoutId
    }

}