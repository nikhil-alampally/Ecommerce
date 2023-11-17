package com.firebase.ecommerce.feature_home.presentation

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TextButton
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.firebase.ecommerce.R
import com.firebase.ecommerce.feature_cart.data.repository.OrderDetails
import com.firebase.ecommerce.feature_cart.domain.model.CartItem
import com.firebase.ecommerce.feature_cart.presentation.viewmodel.CartViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

@SuppressLint(
    "CoroutineCreationDuringComposition", "SimpleDateFormat",
    "MutableCollectionMutableState"
)
@Composable
fun Orders(cartViewModel: CartViewModel = hiltViewModel()) {
    var itemsList by remember {
        mutableStateOf<ArrayList<CartItem>?>(null)
    }
    var ordersList by remember {
        mutableStateOf<ArrayList<OrderDetails>?>(null)
    }
    val scope = rememberCoroutineScope()


    LaunchedEffect(key1 = Unit, block = {
        scope.launch {
            cartViewModel.getData()
            cartViewModel.getOrdersData()
            cartViewModel.getDataInState.collect {
                itemsList = it.isSuccess
            }

        }
    })

    scope.launch {
        cartViewModel.getOrdersDataInState.collectLatest {
            ordersList = it.isSuccess
        }
    }


    val sdf =
        SimpleDateFormat("MMM dd uuuu")
    val currentDateAndTime = sdf.format(Date())
    val context = LocalContext.current
    val dateFormat =
        SimpleDateFormat("MMM dd yyyy")
    val date = dateFormat.format(Date())
    itemsList?.forEach { item ->
        cartViewModel.addOrderSummary(
            OrderDetails(
                paymentStatus = stringResource(id = R.string.success),
                cartId = item.title,
                deliveryDate = currentDateAndTime,
                orderDate = date
            ), context
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            stringResource(id = R.string.orderSummary),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(start = 20.dp, top = 20.dp),
            fontSize = 20.sp
        )
        LazyColumn(content = {
            ordersList?.let {
                items(it.toList()) { orderDetails ->

                    itemsList?.forEach { item ->
                        if (orderDetails.cartId == (item.title)) {
                            androidx.compose.material3.Card(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .background(color = Color.White)
                                    .padding(
                                        horizontal = dimensionResource(R.dimen.twenty),
                                        vertical = dimensionResource(R.dimen.ten),
                                    ),
                                colors = CardDefaults.cardColors(Color.White),
                                elevation = CardDefaults.outlinedCardElevation(dimensionResource(id = R.dimen.eight)),
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 10.dp)
                                ) {
                                    Column(Modifier.weight(3f)) {
                                        Text(
                                            text = item.title,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(
                                                top = dimensionResource(
                                                    R.dimen.twenty
                                                )
                                            ),
                                            maxLines = 1
                                        )
                                        Text(
                                            text = item.description,
                                            fontWeight = FontWeight.Normal,
                                            modifier = Modifier,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "Rs:${item.price.toDouble()}",
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .wrapContentSize()
                                                .clickable {})

                                        Divider(modifier = Modifier.fillMaxWidth())

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(top = 10.dp)
                                        ) {
                                            AsyncImage(
                                                model = R.drawable.baseline_check_24,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(dimensionResource(id = R.dimen.fifteen))
                                                    .clip(CircleShape)
                                                    .background(Color(0xffF28500))
                                                    .clickable {

                                                    }
                                            )
                                            Text(
                                                "Order Confirmed on ${orderDetails.orderDate}",
                                                color = Color.Black,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(start = 2.dp)
                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 5.dp)
                                        ) {
                                            Box(
                                                Modifier
                                                    .height(30.dp)
                                                    .width(2.dp)
                                                    .background(Color.LightGray)
                                                    .height(10.dp)
                                                    .padding(start = 40.dp),
                                            )
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            AsyncImage(
                                                model = R.drawable.baseline_check_24,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,

                                                modifier = Modifier
                                                    .size(dimensionResource(id = R.dimen.fifteen))
                                                    .clip(CircleShape)
                                                    .background(Color(0xff3CB043))
                                                    .clickable {

                                                    }
                                            )
                                            Text(
                                                "Delivery Confirmed on ${orderDetails.deliveryDate}",
                                                fontSize = 12.sp,
                                                color = Color.Black,
                                                modifier = Modifier.padding(start = 2.dp)
                                            )
                                        }
                                        TextButton(onClick = {}) {
                                            Text(stringResource(id = R.string.seeAllTheDetails), color = Color(0xff57A0D2))
                                        }
                                    }
                                    AsyncImage(
                                        model = item.images,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .weight(1f)
                                            .padding(dimensionResource(id = R.dimen.ten)),
                                        contentScale = ContentScale.FillBounds,
                                        placeholder = painterResource(id = R.drawable.img)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        })
    }
}
