package com.firebase.ecommerce.feature_home.presentation

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
fun Orders(
    cartViewModel: CartViewModel = hiltViewModel(),
) {
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
        SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z")
    val currentDateAndTime = sdf.format(Date())
    val context = LocalContext.current
    val dateFormat =
        SimpleDateFormat("dd-MM-yyyy")
    val date = dateFormat.format(Date())
    itemsList?.forEach { item ->
        cartViewModel.addOrderSummary(
            OrderDetails(
                addressId = "success",
                cartId = item.title,
                orderSummaryId = currentDateAndTime,
                orderDate = date
            ), context
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            stringResource(id = R.string.orderSummary),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top=20.dp),
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
                                    .height(180.dp)
                                    .background(color = Color.Transparent)
                                    .padding(
                                        horizontal = dimensionResource(R.dimen.twenty),
                                        vertical = dimensionResource(R.dimen.ten),

                                        ),
                                elevation = CardDefaults.outlinedCardElevation(dimensionResource(id = R.dimen.eight)),
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()

                                ) {
                                    AsyncImage(
                                        model = item.images,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(120.dp)
                                            .weight(1f)
                                            .padding(dimensionResource(id = R.dimen.ten)),
                                        contentScale = ContentScale.FillBounds,
                                        placeholder = painterResource(id = R.drawable.img)
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(2f)
                                            .fillMaxHeight()
                                            .padding(start = dimensionResource(R.dimen.ten))

                                    ) {
                                        Text(
                                            text = item.title,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(top = dimensionResource(R.dimen.twenty)),
                                            maxLines = 1
                                        )


                                        Row {
                                            Text(
                                                text = "Rs:${item.price.toDouble()}",
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                                    .wrapContentSize()
                                                    .clickable {})
                                            Text(
                                                text = "(${item.discountPercentage}% off)",
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Red,
                                                modifier = Modifier.padding(
                                                    start = dimensionResource(
                                                        id = R.dimen.ten
                                                    )
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                        }

                                        Text(
                                            text = "quantity:${item.quantity}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(
                                                bottom = dimensionResource(
                                                    id = R.dimen.five
                                                )
                                            )
                                        )
                                        Text(
                                            text = "total:${item.price * item.quantity}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(
                                                bottom = dimensionResource(
                                                    id = R.dimen.five
                                                )
                                            )
                                        )
                                        Text(
                                            text = "DeliveryDate=${orderDetails.orderDate}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(
                                                bottom = dimensionResource(
                                                    id = R.dimen.five
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }
        })
    }

}
