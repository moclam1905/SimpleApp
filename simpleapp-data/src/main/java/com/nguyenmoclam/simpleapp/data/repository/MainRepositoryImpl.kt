package com.nguyenmoclam.simpleapp.data.repository

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import com.nguyenmoclam.simpleapp.database.ItemDao
import com.nguyenmoclam.simpleapp.database.entiry.mapper.asDomain
import com.nguyenmoclam.simpleapp.database.entiry.mapper.asEntity
import com.nguyenmoclam.simpleapp_model.Item
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
    private val itemDao: ItemDao
) : MainRepository {

    @WorkerThread
    override fun fetchItemList(
        page: Int,
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
                var itemList = adapter.fromJson(json) ?: emptyList()

                itemList = itemList.filter { it.page == page }

                itemDao.insertItemList(itemList.asEntity())

                emit(itemDao.getAllItemList(page).asDomain())
            } catch (e: Exception) {
                onError(e.message)
            }

        } else {
            emit(itemDao.getAllItemList(page).asDomain())
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
}
