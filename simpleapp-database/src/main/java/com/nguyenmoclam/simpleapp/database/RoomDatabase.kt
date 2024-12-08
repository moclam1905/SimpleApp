package com.nguyenmoclam.simpleapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nguyenmoclam.simpleapp.database.entiry.ItemEntity

@Database(
    entities = [ItemEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}