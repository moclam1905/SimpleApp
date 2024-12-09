
package com.nguyenmoclam.simpleapp.data.di

import com.nguyenmoclam.simpleapp.data.repository.DetailRepository
import com.nguyenmoclam.simpleapp.data.repository.DetailRepositoryImpl
import com.nguyenmoclam.simpleapp.data.repository.MainRepository
import com.nguyenmoclam.simpleapp.data.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

  @Binds
  fun bindsMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository

  @Binds
  fun bindsDetailRepository(detailRepositoryImpl: DetailRepositoryImpl): DetailRepository
}
