package com.nguyenmoclam.simpleapp.data.repository

import androidx.annotation.WorkerThread
import com.nguyenmoclam.simpleapp.model.Item
import kotlinx.coroutines.flow.Flow

interface DetailRepository {

  @WorkerThread
  fun fetchItemInfo(
    index: Long,
    onComplete: () -> Unit,
    onError: (String?) -> Unit,
  ): Flow<Item>

  @WorkerThread
  suspend fun deleteItem(index: Long)
}
