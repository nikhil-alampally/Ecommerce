package com.firebase.ecommerce.feature_cart.data.repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class OrderDetails(val addressId:String="",val cartId:String="",val orderSummaryId:String="",val orderDate:String=""):Parcelable