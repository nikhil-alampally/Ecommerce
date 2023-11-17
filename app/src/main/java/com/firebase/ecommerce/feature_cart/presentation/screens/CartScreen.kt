package com.firebase.ecommerce.feature_cart.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.ConnectionState
import com.firebase.ecommerce.core.Constants
import com.firebase.ecommerce.core.connectivityState
import com.firebase.ecommerce.feature_cart.domain.model.CartItem
import com.firebase.ecommerce.feature_cart.presentation.viewmodel.CartViewModel
import com.firebase.ecommerce.feature_login.presentation.screens.CustomDialogBox
import com.firebase.ecommerce.navigation.NavRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint(
    "CoroutineCreationDuringComposition", "MutableCollectionMutableState",
    "UnusedMaterial3ScaffoldPaddingParameter"
)
@Composable
fun CartScreen(viewModel: CartViewModel = hiltViewModel(),navController: NavController) {
    var itemsList by remember {
        mutableStateOf<ArrayList<CartItem>?>(null)
    }
    var deletedSuccessful by remember {
        mutableStateOf<CartItem?>(null)
    }
    var totalPrice by remember {
        mutableStateOf(0)
    }
    var totalPriceWithQuantity by remember {
        mutableStateOf(0)
    }
    val scope = rememberCoroutineScope()
    deletedSuccessful?.let {
        itemsList?.remove(it)
    }
    val connection by connectivityState()
    if (connection == ConnectionState.Unavailable) {
        var showDialog by remember {
            mutableStateOf(true)
        }
        CustomDialogBox(
            message = stringResource(id = R.string.NoInternet),
            onCancelButtonClick = { showDialog = true },
            showDialog = showDialog
        )
    }
    LaunchedEffect(key1 = totalPriceWithQuantity, block = {
        itemsList?.toMutableStateList()
    })

    if (totalPrice + totalPriceWithQuantity == 0 && itemsList?.isEmpty() == true) {
        Column {
            scope.launch {
                delay(2000)
            }
            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.Url(
                    url = Constants.UrlPlaceHolder
                )
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = 10
            )
            LottieAnimation(
                composition = composition,
                progress = progress,
                alignment = Alignment.Center
            )
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        scope.launch {
            viewModel.getData()
            viewModel.getDataInState.collect { cartSignInState ->
                itemsList = cartSignInState.isSuccess
                itemsList?.forEach {
                    totalPrice += it.price
                }

            }
        }
    })
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        itemsList?.let { cartItemArrayList ->
            SingleItems2(item = cartItemArrayList,
                onItemDeleted = {
                    deletedSuccessful = it
                    totalPrice -= it.price

                }, itemQuantity = {
                    totalPriceWithQuantity = it

                })
        }
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary.copy(0.9f)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (totalPrice + totalPriceWithQuantity != 0) {
                    Text(
                        text = "Rs: ${totalPrice + totalPriceWithQuantity}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.ten)),
                        color = Color.White
                    )
                    Button(
                        onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set("totalPrice", totalPrice)
                            navController.currentBackStackEntry?.savedStateHandle?.set("totalPriceWithQuantity", totalPriceWithQuantity)
                            navController.navigate(NavRoute.PlaceOrder.route)
                        },
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.five)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0XFFE75480)
                        )
                    ) {
                        Text(text = stringResource(id = R.string.PlaceOrder), color = Color.Black)
                    }
                }
            }
        }

    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SingleItems2(
    item: List<CartItem>,
    viewModel: CartViewModel = hiltViewModel(),
    onItemDeleted: (CartItem) -> Unit,
    itemQuantity: (Int) -> Unit
) {


    var totalPriceWithQuantity by remember {
        mutableStateOf(0)
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    scope.launch {
        viewModel.deleteItemInState.collect {
            it.isSuccess
        }
    }

    LazyColumn(modifier = Modifier.fillMaxHeight(0.83f)) {
        items(item) { item ->
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .height(dimensionResource(id = R.dimen.twoHundred))
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
                            .size(dimensionResource(id = R.dimen.twoHundred))
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
                                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.ten)),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                        }

                        Text(
                            text = "total:${item.price * item.quantity}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.five))
                        )
                        Row(
                            modifier = Modifier
                                .wrapContentSize()
                                .border(2.dp, Color.Gray),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_add_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(dimensionResource(id = R.dimen.five))
                                    .clickable {
                                        item.quantity++
                                        totalPriceWithQuantity += item.price
                                        itemQuantity(totalPriceWithQuantity)
                                    }
                            )
                            Text(item.quantity.toString())

                            Icon(
                                painter = painterResource(id = R.drawable.baseline_horizontal_rule_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(vertical = dimensionResource(id = R.dimen.five), horizontal = dimensionResource(id = R.dimen.five))
                                    .clickable {
                                        if (item.quantity > 1) {
                                            item.quantity--
                                            totalPriceWithQuantity -= item.price
                                            itemQuantity(totalPriceWithQuantity)
                                        }
                                    }
                            )
                        }

                    }
                    Box(
                        modifier = Modifier.wrapContentSize(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Icon(

                            painter = painterResource(id = R.drawable.baseline_delete_24),
                            contentDescription = null,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(dimensionResource(id = R.dimen.ten))
                                .size(dimensionResource(id = R.dimen.thirty))
                                .clickable {
                                    if (item.quantity > 1) {
                                        totalPriceWithQuantity -= (item.price * item.quantity)
                                    }
                                    onItemDeleted(item)
                                    scope.launch {

                                        viewModel.deleteCartItem(
                                            "${item.title}-${item.id}",
                                            context = context
                                        )
                                    }

                                },
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}




