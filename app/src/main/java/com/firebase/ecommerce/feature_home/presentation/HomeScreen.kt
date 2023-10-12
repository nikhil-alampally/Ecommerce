package com.firebase.ecommerce.feature_home.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.ConnectionState
import com.firebase.ecommerce.core.Constants
import com.firebase.ecommerce.core.connectivityState
import com.firebase.ecommerce.feature_home.data.Categories
import com.firebase.ecommerce.feature_home.domain.HomeData
import com.firebase.ecommerce.feature_login.presentation.screens.CustomDialogBox
import com.firebase.ecommerce.feature_profile.presentation.setData
import com.firebase.ecommerce.navigation.NavRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HomeScreenPreview(navController: NavHostController) {
    CardsItems(
        categories = listOf(
            Categories(
                title = stringResource(id = R.string.getTheBestElectronics),
               image =  painterResource(id = R.drawable.electronic_image),
                color = Color.Yellow.copy(alpha = 0.3f),Constants.ELECTRONICS
            ),
            Categories(
                title=stringResource(id = R.string.getTheBestMenShopping),
                image = painterResource(id = R.drawable.men_transformed),
                color = Color(0xff85C1E9 ).copy(alpha = 0.2f),Constants.MEN
            ),
            Categories(
                title= stringResource(id = R.string.getTheBestWomenShopping),
               image =  painterResource(id = R.drawable.women),
                color = Color(0xffF1948A).copy(alpha = 0.2f),Constants.WOMEN
            ),
            Categories(
                title =stringResource(id = R.string.getTheBestSkinCare) ,
               image =  painterResource(id = R.drawable.skincare),
                color = Color(0xff82E0AA).copy(alpha = 0.3f),Constants.SKINCARE
            ),
            Categories(
                title = stringResource(id = R.string.homeAndDecor),
                image = painterResource(id = R.drawable.home),
                color = Color(0xffAEB6BF).copy(alpha = 0.2f),Constants.HOMEDECORE
            )
        ),navController
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsItems(categories: List<Categories>, navController: NavHostController, homeViewModel: HomeViewModel= hiltViewModel()) {
    val scope= rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    var profileData: HomeData? by remember {
        mutableStateOf(null)
    }
    var visible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(1000)
        visible=true

    }
    LaunchedEffect(key1 = Unit, block = {
        scope.launch {
            homeViewModel.getData()
            homeViewModel.getDataInState.collect{
                profileData=it.isSuccess
            }

        }
    })

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
    Column(modifier=Modifier.fillMaxSize()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.ten))
        ) {
            AsyncImage(
                model=profileData?.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.fifty))
                    .clip(CircleShape)
                    .border(dimensionResource(id = R.dimen.four), Color.Gray, CircleShape)
                    .border(4.dp, Color.Gray, CircleShape)
                    .clickable {
                        navController.setData("profile_details",profileData)
                        navController.navigate(NavRoute.ProfileScreen.route) }
            )
            AssistChip(
                onClick = {  },
                label = { Text(stringResource(id = R.string.orders)) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color.LightGray.copy(
                        alpha = 0.3f
                    )
                ),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.ten)),
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.baseline_shopping_cart_24),
                        modifier = Modifier.padding(dimensionResource(R.dimen.ten)),
                        contentDescription = null
                    )
                },
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.twenty)))

        Text(
            text="WELCOME ${profileData?.userName}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(R.dimen.thirty)),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.twenty)))

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically { fullHeight -> fullHeight },


            ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = scrollState
            ) {
                items(categories) {
                    SingleItem(
                        title = it.title,
                        image = it.image,
                        cardColor = it.color,
                        onItemClick = {
                            navController.setData(Constants.apiParameterKey,it.category)
                            navController.navigate(NavRoute.ItemScreen.route)
                        },
                    )
                }

            }
        }

    }
}


@Composable
fun SingleItem(title: String, image: Painter, cardColor: Color,onItemClick:()->Unit={}) {

        Card(
            modifier = Modifier
                .wrapContentSize()
                .height(dimensionResource(R.dimen.oneFifty))
                .background(color = Color.Transparent)
                .padding(
                    horizontal = dimensionResource(R.dimen.twenty),
                    vertical = dimensionResource(R.dimen.ten)
                ),
            shape = RoundedCornerShape(dimensionResource(R.dimen.twentyFive)),
            border = BorderStroke(2.dp, cardColor),
            elevation = CardDefaults.outlinedCardElevation(8.dp),


            ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = cardColor)
                    .clickable {
                        onItemClick.invoke()
                    }

            ) {

                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(dimensionResource(R.dimen.five))
                )


                Column(
                   modifier= Modifier
                       .fillMaxWidth()
                       .weight(2f)
                       .fillMaxHeight()
                       .padding(start = dimensionResource(R.dimen.ten)),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.ten))
                    )
                    Text(
                        text= stringResource(id = R.string.show_now),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .wrapContentSize()
                            .clickable {})
                }
            }
        }
}



fun <T>NavHostController.setData(key: String, value: T){
    currentBackStackEntry?.savedStateHandle?.set(key, value)
}


