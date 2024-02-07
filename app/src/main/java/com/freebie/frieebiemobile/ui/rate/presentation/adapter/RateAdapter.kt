package com.freebie.frieebiemobile.ui.rate.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateCompanyResponseUIModel
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateItemViewType
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateUIModel
import com.freebie.frieebiemobile.ui.rate.presentation.model.UserRateUiModel

class RateAdapter(
    private val clickListener: (RateUIModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(
        this,
        object : DiffUtil.ItemCallback<RateUIModel>() {
            override fun areItemsTheSame(
                oldItem: RateUIModel,
                newItem: RateUIModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RateUIModel,
                newItem: RateUIModel
            ): Boolean {
                return oldItem == newItem
            }
        })

    fun submitList(list: List<RateUIModel>) = differ.submitList(list)

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].getItemViewType().intValue
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            RateItemViewType.COMPANY_RESPONSE.intValue -> {
                inflater.inflate(R.layout.item_rate, parent, false)
                CompanyReviewResponseViewHolder(
                    inflater.inflate(
                        R.layout.item_company_rate_response,
                        parent,
                        false
                    )
                )
            }

            RateItemViewType.USER_REVIEW.intValue -> {
                RateViewHolder(
                    inflater.inflate(R.layout.item_rate, parent, false),
                    clickListener
                )
            }
            else -> error("unsupported view type")
        }
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RateViewHolder -> holder.bind(differ.currentList[position] as UserRateUiModel)
            is CompanyReviewResponseViewHolder -> holder.bind(differ.currentList[position] as RateCompanyResponseUIModel)
        }

    }
}