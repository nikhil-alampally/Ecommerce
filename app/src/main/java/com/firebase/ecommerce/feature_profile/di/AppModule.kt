package com.firebase.ecommerce.feature_profile.di

import com.firebase.ecommerce.feature_login.data.repository.RegistrationRepositoryImp
import com.firebase.ecommerce.feature_login.domain.repository.RegistrationRepository
import com.firebase.ecommerce.feature_profile.data.repository.ProfileRepositoryImpl
import com.firebase.ecommerce.feature_profile.domain.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
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
class AppModule {
    @Provides
    fun provideProfileImageRepository(): ProfileRepository = ProfileRepositoryImpl(
        storage = Firebase.storage,
        db = Firebase.firestore
    )

}