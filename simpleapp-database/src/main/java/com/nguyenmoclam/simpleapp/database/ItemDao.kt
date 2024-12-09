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

    @Query("SELECT EXISTS(SELECT 1 FROM ItemEntity)")
    suspend fun hasItems(): Boolean

    @Query("SELECT * FROM ItemEntity WHERE `index` = :index_")
    suspend fun getItemInfo(index_: Long): ItemEntity

    @Query("SELECT * FROM ItemEntity WHERE page <= :page_ ORDER BY `index` DESC")
    fun getItemsSortedByIndexDesc(page_: Int): List<ItemEntity>

    @Query("SELECT * FROM ItemEntity WHERE page <= :page_ ORDER BY title DESC")
    fun getItemsSortedByTitleDesc(page_: Int): List<ItemEntity>

    @Query("SELECT * FROM ItemEntity WHERE page <= :page_ ORDER BY date DESC")
    fun getItemsSortedByDateDesc(page_: Int): List<ItemEntity>

    @Query("DELETE FROM ItemEntity WHERE `index` = :index")
    suspend fun deleteItemByIndex(index: Long)
}