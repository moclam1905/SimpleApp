
package com.nguyenmoclam.simpleapp.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nguyenmoclam.simpleapp.ui.main.MainViewModel
import com.nguyenmoclam.simpleapp.utils.RecyclerViewPaging
import com.skydoves.bindables.BindingListAdapter

object RecyclerViewBinding {

  @JvmStatic
  @BindingAdapter("adapter")
  fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter.apply {
      stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }
  }

  @JvmStatic
  @BindingAdapter("submitList")
  fun bindSubmitList(view: RecyclerView, itemList: List<Any>?) {
    (view.adapter as? BindingListAdapter<Any, *>).let { adapter ->
      adapter?.submitList(itemList)
    }
  }

  @JvmStatic
  @BindingAdapter("paginationItemList")
  fun paginationItemList(view: RecyclerView, viewModel: MainViewModel) {
    RecyclerViewPaging(
      recyclerView = view,
      isLoading = { viewModel.isLoading },
      loadMore = { viewModel.fetchNextItemList() },
      onLast = { false },
    ).run {
      threshold = 8
    }
  }
}
