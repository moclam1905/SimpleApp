package com.nguyenmoclam.simpleapp.ui.details

import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nguyenmoclam.simpleapp.data.repository.DetailRepository
import com.nguyenmoclam.simpleapp_model.Item
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.asBindingProperty
import com.skydoves.bindables.bindingProperty
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class DetailViewModel @AssistedInject constructor(
    detailRepository: DetailRepository,
    @Assisted private val itemIndex: Long,
) : BindingViewModel() {

    @get:Bindable
    var isLoading: Boolean by bindingProperty(true)
        private set

    @get:Bindable
    var toastMessage: String? by bindingProperty(null)
        private set

    private val itemInfoFlow: Flow<Item?> = detailRepository.fetchItemInfo(
        index = itemIndex,
        onComplete = { isLoading = false },
        onError = { toastMessage = it },
    )

    @get:Bindable
    val itemInfo: Item? by itemInfoFlow.asBindingProperty(viewModelScope, null)

    init {
        Timber.d("init DetailViewModel")
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(itemIndex: Long): DetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            itemIndex: Long,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(itemIndex) as T
            }
        }
    }
}
