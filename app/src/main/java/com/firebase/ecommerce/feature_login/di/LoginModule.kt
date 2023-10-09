package com.firebase.ecommerce.feature_login.di



import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_login.data.repository.RegistrationRepositoryImp
import com.firebase.ecommerce.feature_login.domain.repository.RegistrationRepository
import com.firebase.ecommerce.feature_login.domain.use_case.StoreRegistrationDetailsWithAuthenticationUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {
    @Provides
    @Singleton
    fun provideFireBaseAuthenticationInstance()=FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun authenticationRepoImpl(firebaseAuth: FirebaseAuth,dataStore: StoreData,fireBase:Firebase): RegistrationRepository {
        return RegistrationRepositoryImp(firebaseAuth,dataStore,fireBase)
    }
    @Provides
    @Singleton
    fun storeRegistrationDetailsWithAuthentication(
      repositoryImp: RegistrationRepository
    ): StoreRegistrationDetailsWithAuthenticationUseCase {
        return StoreRegistrationDetailsWithAuthenticationUseCase(repositoryImp)
    }
}