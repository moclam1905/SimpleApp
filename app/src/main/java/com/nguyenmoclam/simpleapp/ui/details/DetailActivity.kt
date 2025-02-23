package com.nguyenmoclam.simpleapp.ui.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nguyenmoclam.simpleapp.R
import com.nguyenmoclam.simpleapp.databinding.ActivityDetailBinding
import com.nguyenmoclam.simpleapp.extensions.argument
import com.nguyenmoclam.simpleapp.model.Item
import com.nguyenmoclam.simpleapp.utils.CoroutineScopeSingleton
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

    binding.fabDelete.setOnClickListener {
      MaterialAlertDialogBuilder(this)
        .setTitle(getString(R.string.confirm_delete))
        .setMessage(getString(R.string.delete_message))
        .setPositiveButton(getString(R.string.yes)) { _, _ ->
          viewModel.deleteCurrentItem(
            onDeleteSuccess = {
              Toast.makeText(
                this,
                getString(R.string.delete_successfully),
                Toast.LENGTH_SHORT,
              )
                .show()
              setResult(RESULT_CODE_DELETED_ITEM)
              finish()
            },
            onDeleteError = { errorMessage ->
              Toast.makeText(
                this,
                getString(R.string.delete_error) + "" + errorMessage,
                Toast.LENGTH_SHORT,
              ).show()
            },
          )
        }
        .setNegativeButton(getString(R.string.no), null)
        .show()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    CoroutineScopeSingleton.cancel()
  }

  companion object {
    internal const val INFO_ITEM = "INFO_ITEM"
    const val REQUEST_CODE_DETAIL = 1001
    const val RESULT_CODE_DELETED_ITEM = 1002

    fun startActivity(transformationLayout: TransformationLayout, item: Item) {
      val context = transformationLayout.context
      if (context is Activity) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(INFO_ITEM, item)
        TransformationCompat.startActivityForResult(
          transformationLayout,
          intent,
          REQUEST_CODE_DETAIL,
        )
      }
    }
  }
}
