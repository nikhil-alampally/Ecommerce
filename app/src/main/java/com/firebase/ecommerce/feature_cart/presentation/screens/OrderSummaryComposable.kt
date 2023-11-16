import android.annotation.SuppressLint
import android.app.Activity
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_cart.data.repository.OrderDetails
import com.firebase.ecommerce.feature_cart.domain.model.CartItem
import com.firebase.ecommerce.feature_cart.presentation.viewmodel.CartViewModel
import com.firebase.ecommerce.feature_placeorder.data.AddAddress
import com.razorpay.Checkout
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Date

@SuppressLint("MutableCollectionMutableState")
@Composable
fun OrderSummary(
    viewModel: CartViewModel = hiltViewModel(),
    activity: Activity,
    navController: NavController,
    addAddress: AddAddress,
) {
    var itemsList by remember {
        mutableStateOf<ArrayList<CartItem>?>(null)
    }

    val shippingCost = 50
    val tax = 10

    val totalPrice: Int? = navController.previousBackStackEntry?.savedStateHandle?.get("totalPrice")
    val totalPriceWithQuantity: Int? =
        navController.previousBackStackEntry?.savedStateHandle?.get("totalPriceWithQuantity")

    val subtotal by remember {
        mutableStateOf((totalPrice ?: 0) + (totalPriceWithQuantity ?: 0))
    }

    val total: Int = remember {
        subtotal + shippingCost + tax
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit, block = {
        scope.launch {
            viewModel.getData()
            viewModel.getDataInState.collect {
                itemsList = it.isSuccess
            }
        }
    })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        Text(text = "Delivery Address", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.size(5.dp))
        Card(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            border = BorderStroke(1.dp, color = Color.LightGray),
            colors = CardDefaults.cardColors(
                Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Row {
                    Text(text = addAddress.houseNumber, fontSize = 15.sp)
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(text = addAddress.area, fontSize = 15.sp)
                }
                Row {
                    Text(text = addAddress.state, fontSize = 15.sp)
                }
                Row {
                    Text(text = addAddress.pincode, fontSize = 15.sp)
                }
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "Order Summary", fontSize = 16.sp, fontWeight = FontWeight.Bold)

        if (itemsList?.isNotEmpty() == true) {
            itemsList?.let {
                OrderSummaryItem(it)
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        CostDetails(label = "Subtotal", value = subtotal, fontWeight = FontWeight.Bold)
        CostDetails(label = "Shipping Cost", value = shippingCost)
        CostDetails(label = "Tax", value = tax)
        Spacer(modifier = Modifier.size(20.dp))
        CostDetails(label = "Total", value = total, fontWeight = FontWeight.Bold, font = 20.sp)
        Spacer(modifier = Modifier.size(5.dp))
        Row() {
            Button(
                onClick = {
                    makePayment(activity, total.toString())
                          }, modifier = Modifier
                    .align(CenterVertically)
                    .padding(start = 100.dp, end = 50.dp)
            ) {
                Text(text = "Go to Payment")
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun OrderSummaryItem(
    item: List<CartItem>,
    viewModel: CartViewModel = hiltViewModel(),

) {

    val scope = rememberCoroutineScope()

    scope.launch {
        viewModel.deleteItemInState.collect {
            it.isSuccess
        }
    }

    LazyColumn(modifier = Modifier.fillMaxHeight(0.65f)) {
        items(item) { item ->

            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .height(120.dp)
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
                    }
                }
            }
        }
    }
}

@Composable
fun CostDetails(
    label: String,
    value: Int,
    fontWeight: FontWeight = FontWeight.Normal,
    font: TextUnit = 16.sp,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = fontWeight, fontSize = font)
        Text(text = "Rs: $value", fontWeight = fontWeight, fontSize = font)
    }
}

fun makePayment(activity: Activity, amount: String) {
    val co = Checkout()


    try {
        val options = JSONObject()
        options.put("name", "Ecommerce App")
        options.put("description", "Demoing Charges")
        options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
        options.put("theme.color", "#3399cc")
        options.put("currency", "INR")
        options.put("amount", amount + "00")


        val prefill = JSONObject()
        prefill.put("email", "")
        prefill.put("contact", "")

        options.put("prefill", prefill)
        co.open(activity, options)

    } catch (e: Exception) {
        Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
}

