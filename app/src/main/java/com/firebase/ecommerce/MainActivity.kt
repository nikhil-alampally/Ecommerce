package com.firebase.ecommerce

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.firebase.ecommerce.feature_cart.presentation.screens.PaymentSuccessScreen
import com.firebase.ecommerce.navigation.NavGraph
import com.firebase.ecommerce.ui.theme.EcommerceTheme
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultListener {

    private var isPaymentSuccessful by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isPaymentSuccessful) {
                        PaymentSuccessScreen()
                    } else {
                            NavGraph(this, this)
                        }

                }
            }
        }

    }
    override fun onPaymentSuccess(p0: String?) {
        isPaymentSuccessful = true
        Toast.makeText(this, "Payment successful $p0", Toast.LENGTH_SHORT).show()
      /*  val navController=NavController(this)
        navController.navigate(NavRoute.HomeScreen.route)*/
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        isPaymentSuccessful = false
        Toast.makeText(this, "Error $p1", Toast.LENGTH_SHORT).show()
    }
}

