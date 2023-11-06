package com.firebase.ecommerce.feature_products.presentation.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.ConnectionState
import com.firebase.ecommerce.core.Constants
import com.firebase.ecommerce.core.connectivityState
import com.firebase.ecommerce.feature_cart.domain.model.CartItem
import com.firebase.ecommerce.feature_cart.presentation.viewmodel.CartViewModel

import com.firebase.ecommerce.feature_home.presentation.screen.setData
import com.firebase.ecommerce.feature_login.presentation.screens.CustomDialogBox
import com.firebase.ecommerce.feature_products.domain.model.Product
import com.firebase.ecommerce.feature_products.presentation.viewmodel.ProductViewModel
import kotlinx.coroutines.launch





@SuppressLint("MutableCollectionMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    category: String,
    navController: NavHostController,
    onItemClick: () -> Unit = {}
) {

        val connection by connectivityState()
        if (connection == ConnectionState.Unavailable) {
            var showDialog by remember {
                mutableStateOf(true)
            }
            CustomDialogBox(
                message = stringResource(id = R.string.NoInternet),
                onCancelButtonClick = { showDialog = false },
                showDialog = showDialog
            )
        }
    var cartItemsList by remember {
        mutableStateOf<ArrayList<CartItem>?>(null)
    }

            val items = viewModel.getData.collectAsState(initial = null)


            val scope = rememberCoroutineScope()
            var itemsList by remember {
                mutableStateOf<List<Product>?>(null)
            }
            var chipsListCategory by remember {
                mutableStateOf<List<String>?>(null)
            }
            LaunchedEffect(key1 = Unit, key2 = viewModel.selectedCategory.value, block = {
                viewModel.getProducts(category)
                cartViewModel.getData()

            })

            LaunchedEffect(key1 = items.value?.products, block = {
                scope.launch {

                    if (items.value?.products?.isNotEmpty() == true) {
                        itemsList = items.value!!.products
                        val categoryList= itemsList?.map { it.category }?.distinct()
                        val newList = mutableListOf("All")
                        if (categoryList != null) {
                            newList.addAll(categoryList)
                        }
                        if (viewModel.selectedCategory.value == "All") {
                            chipsListCategory = newList
                        }

                    }
                }
            })

            Column {
                Row {
                    chipsListCategory?.let { stringList ->
                        Categories(
                            categories = stringList,
                            onSelectCategory = {
                                viewModel.setCategory(it)
                            },
                            selectedCategory = viewModel.selectedCategory.value,
                            navController = navController
                        )
                    }
                }
                LazyVerticalGrid(columns = GridCells.Fixed(2), content = {

                    scope.launch {
                        cartViewModel.getDataInState.collect{
                            cartItemsList=it.isSuccess
                        }
                    }

                        val cartItemIds = cartItemsList?.map { it.title }


                    Log.e("cartItemId's",cartItemIds.toString())


                    itemsList?.size?.let {
                        println(itemsList!!.size.toString())
                        items(itemsList!!) {
                           val goToCart= cartItemIds?.contains(it.title)

                            SingleItems(item = it, onItemClick = {
                                navController.setData(Constants.ITEMSLIST, it)
                                onItemClick()
                            }, viewModel,goToCart,navController)
                        }
                    }
                })
            }


}




@Composable
fun SingleItems(
    item: Product,
    onItemClick: () -> Unit = {},
    viewModel: ProductViewModel = hiltViewModel(),
    goToCart: Boolean?,
    navController: NavHostController,
cartViewModel: CartViewModel = hiltViewModel()
) {
    var colorChange by remember {
        mutableStateOf(false)
    }
    var count by remember {
        mutableStateOf(0)
    }

    val scope= rememberCoroutineScope()
    val context= LocalContext.current

    Card(
        modifier = Modifier
            .wrapContentSize()
            .height(dimensionResource(id = R.dimen.twoEighty))
            .background(color = Color.Transparent)
            .padding(
                start = dimensionResource(R.dimen.twenty),
                end = dimensionResource(R.dimen.twenty),
                bottom = dimensionResource(R.dimen.ten),
                top = dimensionResource(R.dimen.ten)
            ),
        elevation = CardDefaults.outlinedCardElevation(dimensionResource(id = R.dimen.eight)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onItemClick.invoke()
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.five)),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = if (colorChange) painterResource(id = R.drawable.baseline_favorite_24) else painterResource(
                        id = R.drawable.baseline_favorite_24
                    ), contentDescription = null, modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            colorChange = !colorChange
                            if (colorChange) {
                                viewModel.addToWishlistData(item)
                            }
                            else{
                                viewModel.deleteWishlistItem("${item.title}-${item.id}")
                            }
                        }, tint = if (colorChange) Color.Red else Color.Gray
                )
            }


            AsyncImage(
                model = item.images,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.twoHundred))
                    .weight(2f)
                    .padding(dimensionResource(R.dimen.five)),
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(id = R.drawable.img)
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(start = dimensionResource(R.dimen.ten)),

                ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier,
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

                RatingBarHalfStar(currentRating = item.rating.toFloat(), onRatingChanged = {})
                Button(
                    onClick = {
                       count++
                        if(goToCart==true){
                        navController.popBackStack()
                        }
                        viewModel.storeAddToCartData(product=item, context = context)
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxWidth()
                        .padding(
                            end = dimensionResource(id = R.dimen.ten),
                            top = dimensionResource(id = R.dimen.two),
                            bottom = dimensionResource(id = R.dimen.two)
                        ),
                ) {
                    if(goToCart==true||count==1){
                        Text(stringResource(id = R.string.goToCart))
                    }
                    else {
                        Text(stringResource(id = R.string.addToCart))
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Categories(
    categories: List<String>,
    onSelectCategory: (String) -> Unit,
    selectedCategory: String,
    navController: NavHostController

) {


    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.thirty))
                .padding(start = dimensionResource(id = R.dimen.ten))
                .clickable {
                    navController.popBackStack()

                })
        LazyRow(
            Modifier,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.eight)),
        ) {

            items(categories) { category ->

                AssistChip(
                    onClick = {
                        onSelectCategory(category)
                    },
                    label = { Text(category) },
                    colors = if (category == selectedCategory) {
                        AssistChipDefaults.assistChipColors(
                            containerColor = Color.Yellow.copy(
                                alpha = 0.3f
                            )
                        )
                    } else {
                        AssistChipDefaults.assistChipColors(
                            containerColor = Color.LightGray.copy(
                                alpha = 0.3f
                            )
                        )

                    },
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.ten)),

                    )

            }
        }

    }
}

@Composable
fun RatingBarHalfStar(
    maxRating: Int = 5,
    currentRating: Float,
    onRatingChanged: (Float) -> Unit,
    size: Int = 20
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.four))
    ) {
        for (rating in 1..maxRating) {
            var count by remember {
                mutableStateOf(0)
            }
            val isSelected = rating <= currentRating
            val isHalfFilled = rating == currentRating.toInt() && currentRating % 1 > 0
            StarIcon(
                isSelected = isSelected || isHalfFilled,
                isHalfFilled = isHalfFilled,
                onClick = {
                    count++
                    onRatingChanged(
                        if (count == 2) {
                            rating.toFloat()
                        } else {
                            rating + 0.5f
                        }

                    )
                },
                isHalfSelected = true,
                drawable = 0,
                size = size
            )
        }
    }
}

@Composable
fun StarIcon(
    isSelected: Boolean,
    onClick: () -> Unit,
    isHalfSelected: Boolean = false,
    isHalfFilled: Boolean,
    drawable: Int,
    size: Int = 20
) {

    Icon(
        painter = painterResource(
            id = if (isHalfSelected) {
                if (isHalfFilled) R.drawable.baseline_star_half_24 else R.drawable.baseline_star_24
            } else drawable
        ),
        contentDescription = null,
        tint = if (isSelected) Color.Black else Color.Gray,
        modifier = Modifier
            .size(size.dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.four))
            )
            .pointerInput(Unit) {
                detectTapGestures {
                    onClick()
                }
            }
    )
}


