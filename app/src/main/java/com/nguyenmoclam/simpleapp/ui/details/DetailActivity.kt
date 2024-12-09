package com.nguyenmoclam.simpleapp.ui.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import com.nguyenmoclam.simpleapp.R
import com.nguyenmoclam.simpleapp.databinding.ActivityDetailBinding
import com.nguyenmoclam.simpleapp.extensions.argument
import com.nguyenmoclam.simpleapp_model.Item
import com.skydoves.bindables.BindingActivity
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationCompat.onTransformationEndContainerApplyParams
import com.skydoves.transformationlayout.TransformationLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : BindingActivity<ActivityDetailBinding>(R.layout.activity_detail) {

    @Inject
    internal lateinit var detailViewModelFactory: DetailViewModel.AssistedFactory

    @get:VisibleForTesting
    internal val viewModel: DetailViewModel by viewModels {
        DetailViewModel.provideFactory(detailViewModelFactory, item.index)
    }

    private val item: Item by argument(INFO_ITEM)

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainerApplyParams(this)
        super.onCreate(savedInstanceState)
        binding.item = item
        binding.vm = viewModel
    }

    companion object {
        @VisibleForTesting
        internal const val INFO_ITEM = "INFO_ITEM"

        fun startActivity(transformationLayout: TransformationLayout, item: Item) {
            val context = transformationLayout.context
            if (context is Activity) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(INFO_ITEM, item)
                TransformationCompat.startActivity(transformationLayout, intent)
            }
        }
    }
}