package com.firebase.ecommerce.feature_placeorder.di

import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_placeorder.data.PlaceOrderRepositoryImp
import com.firebase.ecommerce.feature_placeorder.domain.PlaceOrderRepository
import com.firebase.ecommerce.feature_placeorder.presentaion.AddAddressUseCase
import com.firebase.ecommerce.feature_placeorder.presentaion.AddAddressViewModel
import com.firebase.ecommerce.feature_placeorder.presentaion.DeleteAddressFromFirebase
import com.firebase.ecommerce.feature_placeorder.presentaion.GetAddressUseCase
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaceOrderModule {

    @Provides
    @Singleton
    fun placeOrderRepoImpl(fireBase:Firebase,dataStore: StoreData): PlaceOrderRepository {
        return PlaceOrderRepositoryImp(fireBase,dataStore)
    }

    @Provides
    @Singleton
    fun AddAddress_UseCase(placeOrderRepository: PlaceOrderRepository):AddAddressUseCase{
        return AddAddressUseCase(placeOrderRepository)
    }
    @Provides
    @Singleton
    fun GetAddressUseCase(placeOrderRepository: PlaceOrderRepository):GetAddressUseCase{
        return com.firebase.ecommerce.feature_placeorder.presentaion.GetAddressUseCase(placeOrderRepository)
    }
    @Provides
    @Singleton
    fun DeleteAddressUseCase(placeOrderRepository: PlaceOrderRepository):DeleteAddressFromFirebase{
        return DeleteAddressFromFirebase(placeOrderRepository)
    }

    @Provides
    @Singleton
    fun AddAddressViewModel(addAddressUseCase: AddAddressUseCase,getAddressUseCase: GetAddressUseCase,deleteAddressFromFirebase: DeleteAddressFromFirebase):AddAddressViewModel{
        return com.firebase.ecommerce.feature_placeorder.presentaion.AddAddressViewModel(addAddressUseCase = addAddressUseCase,getAddressUseCase, deleteAddressFromFirebase =deleteAddressFromFirebase )
    }



}