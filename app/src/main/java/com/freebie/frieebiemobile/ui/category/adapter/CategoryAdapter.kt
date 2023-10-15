package com.freebie.frieebiemobile.ui.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.category.model.CategoryItem
import com.freebie.frieebiemobile.ui.category.model.CategoryUI
import com.freebie.frieebiemobile.ui.category.model.CategoryViewType
import com.freebie.frieebiemobile.ui.category.viewholder.CategoryShimmerViewHolder
import com.freebie.frieebiemobile.ui.category.viewholder.CategoryViewHolder
import java.lang.IllegalArgumentException

class CategoryAdapter(
    private val clickListener: (CategoryItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem
        }

    })

    fun submitList(items: List<CategoryItem>) = differ.submitList(items)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            CategoryViewType.CATEGORY.ordinal -> CategoryViewHolder(
                inflater.inflate(R.layout.item_category_group, parent, false),
                clickListener
            )
            CategoryViewType.SHIMMER.ordinal -> CategoryShimmerViewHolder(
                inflater.inflate(R.layout.item_category_shimmer, parent, false)
            )
            else -> throw IllegalArgumentException("invalid view type")
        }
    }

    override fun getItemViewType(position: Int) =
        differ.currentList[position].getItemViewType()

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> holder.bind(differ.currentList[position] as CategoryUI)
        }
    }
}