package com.nguyenmoclam.simpleapp.database.di

import android.app.Application
import androidx.room.Room
import com.nguyenmoclam.simpleapp.database.ItemDao
import com.nguyenmoclam.simpleapp.database.RoomDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

  @Provides
  @Singleton
  fun provideMoshi(): Moshi {
    return Moshi.Builder()
      .addLast(KotlinJsonAdapterFactory())
      .build()
  }

  @Provides
  @Singleton
  fun provideAppDatabase(
    application: Application
  ): RoomDatabase {
    return Room
      .databaseBuilder(application, RoomDatabase::class.java, "Item.db")
      .fallbackToDestructiveMigration()
      .build()
  }

  @Provides
  @Singleton
  fun provideItemDao(appDatabase: RoomDatabase): ItemDao {
    return appDatabase.itemDao()
  }

}
