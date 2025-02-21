package com.nguyenmoclam.simpleapp.data.repository

import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import com.nguyenmoclam.simpleapp.database.ItemDao
import com.nguyenmoclam.simpleapp.database.entiry.mapper.asDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.withContext
import javax.inject.Inject

@VisibleForTesting
class DetailRepositoryImpl @Inject constructor(
  private val itemDao: ItemDao,
) : DetailRepository {

  @WorkerThread
  override fun fetchItemInfo(index: Long, onComplete: () -> Unit, onError: (String?) -> Unit) =
    flow {
      try {
        emit(itemDao.getItemInfo(index).asDomain())
      } catch (e: Exception) {
        onError(e.message)
      }
    }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

  @WorkerThread
  override suspend fun deleteItem(index: Long) {
    withContext(Dispatchers.IO) {
      itemDao.deleteItemByIndex(index)
    }
  }
}
