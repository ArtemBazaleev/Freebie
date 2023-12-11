package com.freebie.frieebiemobile.ui.feed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.feed.viewholder.CouponViewHolder

class CouponsAdapter(
    private val clickListener: (CouponUI) -> Unit
) : RecyclerView.Adapter<CouponViewHolder>() {

    private val mCurrentList: MutableList<CouponUI> = mutableListOf()

    fun submitList(list: List<CouponUI>) {
        mCurrentList.clear()
        mCurrentList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        return CouponViewHolder(
            clickListener,
            LayoutInflater.from(parent.context).inflate(R.layout.item_coupon, parent, false)
        )
    }

    override fun getItemCount() = mCurrentList.size

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        holder.bind(mCurrentList[position])
    }
}