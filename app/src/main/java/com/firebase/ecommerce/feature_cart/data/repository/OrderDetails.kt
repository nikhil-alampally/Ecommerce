package com.firebase.ecommerce.feature_cart.data.repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class OrderDetails(val paymentStatus:String="",val cartId:String="",val deliveryDate:String="",val orderDate:String=""):Parcelable