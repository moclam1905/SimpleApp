package com.nguyenmoclam.simpleapp.data.repository

import androidx.annotation.WorkerThread
import com.nguyenmoclam.simpleapp.data.SortOption
import com.nguyenmoclam.simpleapp.model.Item
import kotlinx.coroutines.flow.Flow

interface MainRepository {

  @WorkerThread
  fun fetchItemList(
    page: Int,
    sortOption: SortOption,
    onStart: () -> Unit,
    onComplete: () -> Unit,
    onError: (String?) -> Unit,
  ): Flow<List<Item>>
}
