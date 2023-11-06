package com.firebase.ecommerce.feature_wishlist.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.firebase.ecommerce.R
import com.firebase.ecommerce.feature_products.presentation.screens.RatingBarHalfStar
import com.firebase.ecommerce.feature_wishlist.domain.WishlistItemModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint(
    "CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter"
)
@Composable
fun WishlistScreen(
    viewModel: WishListViewModel = hiltViewModel(),
    navHostController: NavHostController,
) {
    var itemsList by remember {
        mutableStateOf<ArrayList<WishlistItemModel>?>(null)
    }
    var deletedSuccessful by remember {
        mutableStateOf<WishlistItemModel?>(null)
    }
    val scope = rememberCoroutineScope()

    deletedSuccessful?.let {
        itemsList?.remove(it)
    }

    LaunchedEffect(key1 = Unit, block = {
        scope.launch {
            viewModel.getData()
            viewModel.getDataInState.collect {
                itemsList = it.isSuccess
                Log.e("wishlist data", it.isSuccess.toString())
            }
        }
    })

    if (itemsList?.isNotEmpty() == true) {
        itemsList?.let {
            WishlistSingleItems(it, onItemDeleted = {
                deletedSuccessful = it
            })
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WishlistSingleItems(
    items: List<WishlistItemModel>,
    wishListViewModel: WishListViewModel = hiltViewModel(),
    onItemDeleted: (WishlistItemModel) -> Unit,
) {
    val scope = rememberCoroutineScope()
    scope.launch {
        wishListViewModel.deleteItemInState.collect {
            it.isSuccess
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(bottom = 40.dp),
        content = {
            items(items) { item ->
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
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(id = R.dimen.five)),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .clickable {
                                        onItemDeleted.invoke(item)
                                        scope.launch {
                                            wishListViewModel.deleteWishlistItem("${item.title}-${item.id}")
                                        }
                                    },
                            )
                        }
                        AsyncImage(
                            model = item.images,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimensionResource(id = R.dimen.twoHundred))
                                .weight(3f)
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
                                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.five)),
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
                            RatingBarHalfStar(
                                currentRating = item.rating.toFloat(),
                                onRatingChanged = {})
                        }
                    }
                }
            }
        })
}

