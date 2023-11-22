package com.firebase.ecommerce

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.firebase.ecommerce.navigation.NavGraph
import com.firebase.ecommerce.navigation.NavRoute
import com.firebase.ecommerce.ui.theme.EcommerceTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultListener {

    private var isPaymentSuccessful by mutableStateOf(false)

    var navController : NavHostController? = null
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberAnimatedNavController()
                    NavGraph(this@MainActivity,
                        this,
                        navController!!)

                }
            }
        }

    }
    override fun onPaymentSuccess(paymentId: String?) {
        try {
            Toast.makeText(this, "Payment successful $paymentId", Toast.LENGTH_SHORT).show()
            navController?.navigate(NavRoute.Orders.route)
        } catch (e: Exception) {
            Log.e("NavigationError", "Error navigating to home screen: ${e.message}", e)
        }
    }

    override fun onPaymentError(paymentId: Int, paymentError: String?) {
        isPaymentSuccessful = false
        Toast.makeText(this, "Error $paymentError", Toast.LENGTH_SHORT).show()
    }
}