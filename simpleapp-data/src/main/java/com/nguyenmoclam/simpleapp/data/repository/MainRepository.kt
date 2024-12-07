package com.nguyenmoclam.simpleapp.data.repository

import androidx.annotation.WorkerThread
import com.nguyenmoclam.simpleapp_model.Item
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    @WorkerThread
    fun fetchPokemonList(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
    ): Flow<List<Item>>
}
