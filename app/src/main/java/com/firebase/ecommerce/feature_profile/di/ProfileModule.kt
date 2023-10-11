package com.firebase.ecommerce.feature_profile.di

import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_profile.data.repository.ProfileRepositoryImpl
import com.firebase.ecommerce.feature_profile.domain.repository.ProfileRepository
import com.firebase.ecommerce.feature_profile.domain.use_cases.SaveUserDetailsUseCase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {
    @Provides
    fun provideProfileImageRepository(dataStore: StoreData): ProfileRepository = ProfileRepositoryImpl(
        storage = Firebase.storage,
        db = Firebase.firestore,
        dataStore = dataStore
    )

    @Provides
    @Singleton
    fun getDataProfileScreen(
        profileRepository: ProfileRepository
    ): SaveUserDetailsUseCase {
        return SaveUserDetailsUseCase(profileRepository=profileRepository)
    }


}