package com.nguyenmoclam.simpleapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nguyenmoclam.simpleapp.database.entiry.ItemEntity

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemList(itemList: List<ItemEntity>)

    @Query("SELECT * FROM ItemEntity WHERE page = :page_")
    suspend fun getItemList(page_: Int): List<ItemEntity>

    @Query("SELECT * FROM ItemEntity WHERE page <= :page_")
    suspend fun getAllItemList(page_: Int): List<ItemEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM ItemEntity)")
    suspend fun hasItems(): Boolean

    @Query("SELECT * FROM ItemEntity WHERE `index` = :index_")
    suspend fun getItemInfo(index_: Long): ItemEntity
}