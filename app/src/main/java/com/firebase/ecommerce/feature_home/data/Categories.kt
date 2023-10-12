package com.firebase.ecommerce.feature_home.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

data class Categories(
    val title: String,
    val image: Painter,
    val color: Color,
    val category:String
) {

}