package com.nguyenmoclam.simpleapp.ui.details

import androidx.annotation.VisibleForTesting
import com.nguyenmoclam.simpleapp.R
import com.nguyenmoclam.simpleapp.databinding.ActivityDetailBinding
import com.nguyenmoclam.simpleapp_model.Item
import com.skydoves.bindables.BindingActivity
import com.skydoves.bundler.intentOf
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailActivity : BindingActivity<ActivityDetailBinding>(R.layout.activity_detail) {
    companion object {
        @VisibleForTesting
        internal const val EXTRA_ITEM = "EXTRA_ITEM"

        fun startActivity(transformationLayout: TransformationLayout, item: Item) =
            transformationLayout.context.intentOf<DetailActivity> {
                putExtra(EXTRA_ITEM to item)
                TransformationCompat.startActivity(transformationLayout, intent)
            }
    }
}