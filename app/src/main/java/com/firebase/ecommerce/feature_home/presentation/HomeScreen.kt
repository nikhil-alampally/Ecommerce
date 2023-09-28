package com.firebase.ecommerce.feature_home.presentation

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.firebase.ecommerce.R
import com.firebase.ecommerce.feature_home.data.HomeDataDto
import com.firebase.ecommerce.feature_home.domain.HomeData
import com.firebase.ecommerce.navigation.NavRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HomeScreenPreview(navController: NavHostController) {
    CardsItems(
        categories = listOf(
            Categories(
                "GET THE BEST ELECTRONICS",
                painterResource(id = R.drawable.electronic_image),
                color = Color.Yellow.copy(alpha = 0.3f),100
            ),
            Categories(
                "GET THE BEST MEN SHOPPING",
                painterResource(id = R.drawable.men_transformed),
                color = Color(0xff85C1E9 ).copy(alpha = 0.2f),500
            ),
            Categories(
                "GET THE BEST WOMEN SHOPPING",
                painterResource(id = R.drawable.women),
                color = Color(0xffF1948A).copy(alpha = 0.2f),1000
            ),
            Categories(
                " SKINCARE AND PERFUMES SHOPPING",
                painterResource(id = R.drawable.skincare),
                color = Color(0xff82E0AA).copy(alpha = 0.3f),1500
            ),
            Categories(
                "HOME AND DECOR SHOPPING",
                painterResource(id = R.drawable.home),
                color = Color(0xffAEB6BF).copy(alpha = 0.2f),2000
            )
        ),navController
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsItems(categories: List<Categories>, navController: NavHostController,homeViewModel: HomeViewModel= hiltViewModel()) {
    val scope= rememberCoroutineScope()
    var profileData: HomeData? by remember {
        mutableStateOf(null)
    }
    LaunchedEffect(key1 = Unit, block = {
        scope.launch {
            homeViewModel.getData()
            homeViewModel.getDataInState.collect{
                profileData=it.isSuccess
            }

        }
    })


    Column(modifier=Modifier.fillMaxSize()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            AsyncImage(
                profileData?.image,
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.Gray, CircleShape)
            )
            AssistChip(
                onClick = { Log.d("Assist chip", "hello world") },
                label = { Text("orders") },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color.LightGray.copy(
                        alpha = 0.3f
                    )
                ),
                shape = RoundedCornerShape(10.dp),
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.baseline_shopping_cart_24),
                        modifier = Modifier.padding(10.dp),
                        contentDescription = null
                    )
                },
               // modifier = Modifier.padding(5.dp)


            )
        }
        Spacer(modifier = Modifier.height(20.dp))

            Text(
                "WELCOME ${profileData?.userName}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn() {
                items(categories) {
                    SingleItem(
                        title = it.title,
                        image = it.image,
                        cardColor = it.color,

                        onItemClick = {
                            navController.navigate(NavRoute.ItemScreen.route)
                        }, delay = it.delay
                    )
                }

            }

    }
}


@Composable
fun SingleItem(title: String, image: Painter, cardColor: Color,onItemClick:()->Unit={},delay: Long) {

    var visible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(delay)
        visible=true

    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { fullHeight -> fullHeight },


        ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .height(150.dp)
                .background(color = Color.Transparent)
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(25.dp),
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
                        .padding(5.dp)
                )


                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(2f)
                        .fillMaxHeight()
                        .padding(start = 10.dp), verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Text(
                        "Show Now",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .wrapContentSize()
                            .clickable {

                            })
                }

            }


        }
    }


}


data class Categories(
    val title: String,
    val image: Painter,
    val color: Color,
    val delay: Long
) {

}

