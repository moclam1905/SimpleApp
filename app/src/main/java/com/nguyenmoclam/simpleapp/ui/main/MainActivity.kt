package com.nguyenmoclam.simpleapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import com.nguyenmoclam.simpleapp.R
import com.nguyenmoclam.simpleapp.binding.ViewBinding
import com.nguyenmoclam.simpleapp.data.SortOption
import com.nguyenmoclam.simpleapp.databinding.ActivityMainBinding
import com.nguyenmoclam.simpleapp.ui.details.DetailActivity.Companion.REQUEST_CODE_DETAIL
import com.nguyenmoclam.simpleapp.ui.details.DetailActivity.Companion.RESULT_CODE_DELETED_ITEM
import com.skydoves.bindables.BindingActivity
import com.skydoves.transformationlayout.onTransformationStartContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    @get:VisibleForTesting
    internal val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)
        binding {
            adapter = ItemAdapter()
            vm = viewModel
        }

        binding.mainToolbar.findViewById<ImageView>(R.id.actionSort).setOnClickListener {
            handlePopupMenu(it)
        }
    }

    private fun handlePopupMenu(view: View) {
        val popupContext = ContextThemeWrapper(this, R.style.PopupMenuTheme)
        val popup = PopupMenu(popupContext, view)
        popup.menuInflater.inflate(R.menu.menu_sort, popup.menu)

        when (viewModel.currentSortOption.value) {
            SortOption.INDEX_DESC -> popup.menu.findItem(R.id.sort_by_index)?.isChecked = true
            SortOption.TITLE_DESC -> popup.menu.findItem(R.id.sort_by_title)?.isChecked = true
            SortOption.DATE_DESC -> popup.menu.findItem(R.id.sort_by_date)?.isChecked = true
        }

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort_by_index -> {
                    viewModel.setSortOption(SortOption.INDEX_DESC)
                    true
                }

                R.id.sort_by_title -> {
                    viewModel.setSortOption(SortOption.TITLE_DESC)
                    true
                }

                R.id.sort_by_date -> {
                    viewModel.setSortOption(SortOption.DATE_DESC)
                    true
                }

                else -> false
            }
        }
        popup.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        ViewBinding.CoroutineScopeSingleton.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAIL && resultCode == RESULT_CODE_DELETED_ITEM) {
            viewModel.setSortOption(viewModel.currentSortOption.value)
        }
    }
}