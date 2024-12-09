package com.nguyenmoclam.simpleapp.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import com.nguyenmoclam.simpleapp.R
import com.nguyenmoclam.simpleapp.binding.ViewBinding
import com.nguyenmoclam.simpleapp.databinding.ActivityMainBinding
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
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hủy CoroutineScope để tránh rò rỉ bộ nhớ
        ViewBinding.CoroutineScopeSingleton.cancel()
    }
}