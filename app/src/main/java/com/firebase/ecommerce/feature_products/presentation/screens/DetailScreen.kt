package com.firebase.ecommerce.feature_products.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.ConnectionState
import com.firebase.ecommerce.core.connectivityState
import com.firebase.ecommerce.feature_cart.domain.model.CartItem
import com.firebase.ecommerce.feature_cart.presentation.viewmodel.CartViewModel
import com.firebase.ecommerce.feature_login.presentation.screens.CustomDialogBox
import com.firebase.ecommerce.feature_products.domain.model.Product
import com.firebase.ecommerce.navigation.NavRoute
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DetailScreen(product: Product, navController: NavController,cartValidation:Boolean,wishListValidation:Boolean){
    val connection by connectivityState()
    if(connection== ConnectionState.Unavailable) {
        var showDialog by remember {
            mutableStateOf(true)
        }
        CustomDialogBox(
            message = stringResource(id = R.string.NoInternet),
            onCancelButtonClick = {showDialog=false},
            showDialog = showDialog)
    }
    var colorChange by remember {
        mutableStateOf(false)
    }

    Column(modifier=Modifier.verticalScroll(rememberScrollState())) {
        
        Icon(
            painter= painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = null,
            modifier = Modifier
                .clickable {
                    navController.popBackStack()
                }
                .size(35.dp))
        
        AsyncImage(model=product.images,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .weight(2f)
            , contentScale = ContentScale.Fit,
        placeholder = painterResource(id = R.drawable.img))
        
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,Alignment.CenterVertically){
            
            Text(text=product.title,
                fontWeight = FontWeight.Bold,
                color= Color.Black,
                modifier = 
                Modifier.padding(vertical = 2.dp, horizontal = 10.dp))
            
            Icon(painter =if(colorChange) painterResource(id = R.drawable.baseline_favorite_24) else painterResource(
                id = R.drawable.baseline_favorite_24
            ),contentDescription = null, modifier = Modifier
                .wrapContentSize()
                .clickable {
                    colorChange = !colorChange
                }
                .size(35.dp), tint = if(colorChange||wishListValidation) Color.Red else Color.LightGray)
        }

        Text(text=product.description, modifier = Modifier.padding(vertical = 2.dp, horizontal = 10.dp))
        
        Row{
            
            Text(
                text = "$ ${product.price.toDouble()}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {}
                    .padding(vertical = 2.dp, horizontal = 10.dp))
            
            Text(
                text= "(${product.discountPercentage}% off)",
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier= Modifier
                    .padding(vertical = 2.dp, horizontal = 10.dp))
        }

        RatingBarHalfStar(
            currentRating = product.rating.toFloat(), 
            onRatingChanged = {}, size = 35
        )

        Button(
            onClick = {
                      if(cartValidation){
                          navController.navigate(NavRoute.HomeScreen.route)
                      }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        {
            Text(
                text= stringResource(id = if(cartValidation)R.string.goToCart else R.string.addToCart),
                modifier = Modifier.padding(vertical =8.dp)
            )
        }
    }
}
