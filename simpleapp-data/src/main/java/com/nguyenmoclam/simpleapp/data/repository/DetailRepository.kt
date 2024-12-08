package com.nguyenmoclam.simpleapp.data.repository

import androidx.annotation.WorkerThread
import com.nguyenmoclam.simpleapp_model.Item
import kotlinx.coroutines.flow.Flow

interface DetailRepository {

  @WorkerThread
  fun fetchItemInfo(
    index: Long,
    onComplete: () -> Unit,
    onError: (String?) -> Unit,
  ): Flow<Item>
}