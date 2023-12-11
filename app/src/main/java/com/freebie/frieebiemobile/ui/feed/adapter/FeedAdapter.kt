package com.freebie.frieebiemobile.ui.feed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.feed.models.CompanyHeaderItem
import com.freebie.frieebiemobile.ui.feed.models.CouponsItem
import com.freebie.frieebiemobile.ui.feed.models.FeedItem
import com.freebie.frieebiemobile.ui.feed.models.FeedItemType
import com.freebie.frieebiemobile.ui.feed.models.OffersItem
import com.freebie.frieebiemobile.ui.feed.viewholder.CompanyHeaderViewHolder
import com.freebie.frieebiemobile.ui.feed.viewholder.CouponsViewHolder
import com.freebie.frieebiemobile.ui.feed.viewholder.FeedShimmerViewHolder
import com.freebie.frieebiemobile.ui.feed.viewholder.OffersViewHolder

class FeedAdapter(
    private val clickListener: FeedClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<FeedItem>() {
        override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
            return oldItem == newItem
        }

    })

    fun submitList(items: List<FeedItem>) = differ.submitList(items)

    override fun getItemViewType(position: Int) =
        differ.currentList[position].getItemType().intValue

    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            FeedItemType.COUPON.intValue -> CouponsViewHolder(
                clickListener,
                inflater.inflate(
                    R.layout.item_coupons,
                    parent,
                    false
                )
            )

            FeedItemType.COMPANY_HEADER.intValue -> CompanyHeaderViewHolder(
                inflater.inflate(
                    R.layout.item_company_header,
                    parent,
                    false
                )
            )

            FeedItemType.OFFER.intValue -> OffersViewHolder(
                inflater.inflate(
                    R.layout.item_offers,
                    parent,
                    false
                )
            )

            FeedItemType.FEED_SHIMMER.intValue -> FeedShimmerViewHolder(
                inflater.inflate(
                    R.layout.item_feed_shimmer,
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("item type for feed is not supported")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CompanyHeaderViewHolder -> holder.onBind(differ.currentList[position] as CompanyHeaderItem)
            is CouponsViewHolder -> holder.bind(differ.currentList[position] as CouponsItem)
            is OffersViewHolder -> holder.bind(differ.currentList[position] as OffersItem)
        }
    }
}