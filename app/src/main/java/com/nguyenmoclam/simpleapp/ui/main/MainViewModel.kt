package com.nguyenmoclam.simpleapp.ui.main

import androidx.annotation.MainThread
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.nguyenmoclam.simpleapp.data.repository.MainRepository
import com.nguyenmoclam.simpleapp_model.Item
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.asBindingProperty
import com.skydoves.bindables.bindingProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : BindingViewModel() {

    @get:Bindable
    var isLoading: Boolean by bindingProperty(false)
        private set

    @get:Bindable
    var toastMessage: String? by bindingProperty(null)
        private set

    private val itemFetchingIndex: MutableStateFlow<Int> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val itemListFlow = itemFetchingIndex.flatMapLatest { page ->
        mainRepository.fetchPokemonList(
            page = page,
            onStart = { isLoading = true },
            onComplete = { isLoading = false },
            onError = { toastMessage = it },
        )
    }

    @get:Bindable
    val itemList: List<Item> by itemListFlow.asBindingProperty(viewModelScope, emptyList())

    init {
        Timber.d("init MainViewModel")
    }

    @MainThread
    fun fetchNextItemList() {
        if (!isLoading) {
            itemFetchingIndex.value++
        }
    }
}
