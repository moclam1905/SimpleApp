package com.nguyenmoclam.simpleapp.ui.main

import android.os.SystemClock
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.nguyenmoclam.simpleapp.R
import com.nguyenmoclam.simpleapp.databinding.ItemSimpleBinding
import com.nguyenmoclam.simpleapp.model.Item
import com.nguyenmoclam.simpleapp.ui.details.DetailActivity
import com.skydoves.bindables.BindingListAdapter
import com.skydoves.bindables.binding

class ItemAdapter : BindingListAdapter<Item, ItemAdapter.ItemViewHolder>(diffUtil) {

  private var onClickedAt = 0L

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
    parent.binding<ItemSimpleBinding>(R.layout.item_simple).let(::ItemViewHolder)

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
    holder.bindItem(getItem(position))

  inner class ItemViewHolder(
    private val binding: ItemSimpleBinding,
  ) : RecyclerView.ViewHolder(binding.root) {

    init {
      binding.root.setOnClickListener {
        val position = bindingAdapterPosition.takeIf { it != NO_POSITION }
          ?: return@setOnClickListener
        val currentClickedAt = SystemClock.elapsedRealtime()
        if (currentClickedAt - onClickedAt > binding.transformationLayout.duration) {
          DetailActivity.startActivity(binding.transformationLayout, getItem(position))
          onClickedAt = currentClickedAt
        }
      }
    }

    fun bindItem(item: Item) {
      binding.item = item
      binding.executePendingBindings()
    }
  }

  companion object {
    private val diffUtil = object : DiffUtil.ItemCallback<Item>() {

      override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
        oldItem.index == newItem.index

      override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
        oldItem == newItem
    }
  }
}
