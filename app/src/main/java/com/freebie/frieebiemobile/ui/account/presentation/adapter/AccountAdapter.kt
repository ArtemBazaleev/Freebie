package com.freebie.frieebiemobile.ui.account.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountActionButtonUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountCompanyUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountCouponsUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountDescUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountHeaderUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountType
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupsUIModel
import com.freebie.frieebiemobile.ui.account.presentation.viewholder.AccountButtonVH
import com.freebie.frieebiemobile.ui.account.presentation.viewholder.AccountCompanyVH
import com.freebie.frieebiemobile.ui.account.presentation.viewholder.AccountCouponsGroupVH
import com.freebie.frieebiemobile.ui.account.presentation.viewholder.AccountCouponsVH
import com.freebie.frieebiemobile.ui.account.presentation.viewholder.AccountDescVH
import com.freebie.frieebiemobile.ui.account.presentation.viewholder.AccountHeaderVH

class AccountAdapter(
    private val accountClickListener: AccountClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<AccountUIModel>() {
        override fun areItemsTheSame(oldItem: AccountUIModel, newItem: AccountUIModel): Boolean {
            Log.d("AsyncListDiffer", "oldItem = $oldItem, newItem = $newItem")
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AccountUIModel, newItem: AccountUIModel): Boolean {
            return oldItem == newItem
        }

    })

    fun submitList(items: List<AccountUIModel>) = differ.submitList(items)

    override fun getItemViewType(position: Int) =
        differ.currentList[position].getItemType().intValue

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            AccountType.HEADER.intValue -> {
                AccountHeaderVH(
                    inflater.inflate(R.layout.item_account_header, parent, false)
                )
            }
            AccountType.DESCRIPTION.intValue -> {
                AccountDescVH(
                    inflater.inflate(R.layout.item_account_description, parent, false)
                )
            }
            AccountType.BUTTON.intValue -> {
                AccountButtonVH(
                    inflater.inflate(R.layout.item_account_action_button, parent, false),
                    clickListener = accountClickListener
                )
            }
            AccountType.COUPON_GROUPS.intValue -> {
                AccountCouponsGroupVH(
                    inflater.inflate(R.layout.item_account_coupon_groups, parent, false),
                    accountClickListener
                )
            }
            AccountType.COUPONS.intValue -> {
                AccountCouponsVH(
                    inflater.inflate(R.layout.item_coupons, parent, false),
                    clickListener = accountClickListener
                )
            }
            AccountType.COMPANY.intValue -> {
                AccountCompanyVH(
                    inflater.inflate(
                        R.layout.item_account_company, parent,
                        false
                    ),
                    clickListener = accountClickListener
                )
            }
            else -> error("Unsupported view type")
        }
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when (holder) {
           is AccountHeaderVH -> holder.bind(differ.currentList[position] as AccountHeaderUIModel)
           is AccountDescVH -> holder.bind(differ.currentList[position] as AccountDescUIModel)
           is AccountButtonVH -> holder.bind(differ.currentList[position] as AccountActionButtonUIModel)
           is AccountCouponsGroupVH -> holder.bind(differ.currentList[position] as CouponGroupsUIModel)
           is AccountCouponsVH -> holder.bind(differ.currentList[position] as AccountCouponsUIModel)
           is AccountCompanyVH -> holder.bind(differ.currentList[position] as AccountCompanyUIModel)
       }
    }
}