package com.nguyenmoclam.simpleapp.data.repository

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import com.nguyenmoclam.simpleapp.data.SortOption
import com.nguyenmoclam.simpleapp.database.ItemDao
import com.nguyenmoclam.simpleapp.database.entiry.mapper.asDomain
import com.nguyenmoclam.simpleapp.database.entiry.mapper.asEntity
import com.nguyenmoclam.simpleapp.model.Item
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import javax.inject.Inject

@VisibleForTesting
class MainRepositoryImpl @Inject constructor(
  @ApplicationContext private val context: Context,
  private val itemDao: ItemDao,
) : MainRepository {

  @WorkerThread
  override fun fetchItemList(
    page: Int,
    sortOption: SortOption,
    onStart: () -> Unit,
    onComplete: () -> Unit,
    onError: (String?) -> Unit,
  ) = flow {
    if (!itemDao.hasItems()) {
      try {
        val json = withContext(Dispatchers.IO) {
          context.assets.open("sample_data_list.json")
            .bufferedReader()
            .use { it.readText() }
        }

        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, Item::class.java)
        val adapter = moshi.adapter<List<Item>>(listType)
        val itemList = adapter.fromJson(json) ?: emptyList()

        val itemListSorted = itemList.sortedByDescending { it.index }
        itemListSorted.forEachIndexed { index, item ->
          item.page = index / 20
        }
        itemDao.insertItemList(itemListSorted.asEntity())

        emit(itemDao.getItemsSortedByIndexDesc(page).asDomain())
      } catch (e: Exception) {
        onError(e.message)
      }
    } else {
      emit(
        when (sortOption) {
          SortOption.INDEX_DESC -> itemDao.getItemsSortedByIndexDesc(page)
          SortOption.TITLE_DESC -> itemDao.getItemsSortedByTitleDesc(page)
          SortOption.DATE_DESC -> itemDao.getItemsSortedByDateDesc(page)
        }.map {
          it.asDomain()
        },
      )
    }
  }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
}
