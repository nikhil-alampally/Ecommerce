package com.firebase.ecommerce.feature_home.di

import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_home.data.HomeRepositoryImp
import com.firebase.ecommerce.feature_home.domain.GetDataHomeScreenUseCase
import com.firebase.ecommerce.feature_home.domain.HomeRepository
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    @Provides
    @Singleton
    fun provideFireBaseAuthenticationInstance()= Firebase
   /* fun context()= this.context()*/
   @Provides
   @Singleton
   fun homeRepoImpl(fireBase:Firebase,dataStore:StoreData): HomeRepository {
       return HomeRepositoryImp(fireBase,dataStore)
   }
    @Provides
    @Singleton
    fun getDataHomeScreen(
        homeRepository: HomeRepository
    ): GetDataHomeScreenUseCase {
        return GetDataHomeScreenUseCase(homeRepository = homeRepository)
    }
}