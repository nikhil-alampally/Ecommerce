package com.firebase.ecommerce.feature_profile.presentation

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.Constants
import com.firebase.ecommerce.feature_home.domain.HomeData
import com.firebase.ecommerce.feature_login.presentation.viewmodels.LoginViewModel
import com.firebase.ecommerce.feature_profile.domain.model.ProfileModel
import com.firebase.ecommerce.navigation.NavRoute
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
    loginModel: LoginViewModel = hiltViewModel(),
    profileData: HomeData,
) {

    var isUserNameEditing by remember { mutableStateOf(false) }

    var isMobileNumberEditing by remember { mutableStateOf(false) }

    var isImageUpdating by remember { mutableStateOf(false) }

    val customTextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.Transparent
    )

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            imageUri?.let {
                viewModel.setImage(it)
            }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview(),
            onResult = {
                if (it != null) {
                    viewModel.setImage(it)
                }
            })
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
        )


    val scope = rememberCoroutineScope()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xfff1e4e0))
            .padding(
                start = 4.dp,
                top = 4.dp,
                end = 4.dp,
            )
            .verticalScroll(rememberScrollState())
    )
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        galleryLauncher.launch(Constants.ALL_IMAGES)
                        scope.launch {
                            modalBottomSheetState.hide()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    androidx.compose.material3.Text(stringResource(R.string.choose_from_gallery))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        cameraLauncher.launch()
                        scope.launch {
                            modalBottomSheetState.hide()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    androidx.compose.material3.Text(stringResource(R.string.choose_from_camera))
                }
            }
        }
    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0xff495d92),
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 180.dp)
                    .clip(shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                    .background(color = Color(0xFAF8F8F8))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 25.dp, end = 25.dp, top = 80.dp)
                ) {
                    if (isUserNameEditing) {
                        viewModel.getHomeDataState.value?.userName?.let {
                            TextField(
                                value = viewModel.getHomeDataState.value!!.userName!!,
                                onValueChange = { newValue ->
                                    viewModel.updateUsername(newValue)
                                },
                                textStyle = TextStyle(
                                    fontSize = 25.sp,
                                    color = Color.Black
                                ),
                                colors = customTextFieldColors,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .height(60.dp)

                            )

                        }
                    } else {
                        viewModel.getHomeDataState.value?.userName?.let {
                            viewModel.getHomeDataState.value!!.userName.let { it1 ->
                                if (it1 != null) {
                                    Text(
                                        text = it1,
                                        fontSize = 25.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .height(60.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.size(20.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xffffffff)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.img_1),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            viewModel.getHomeDataState.value?.gender?.let {
                                Text(
                                    text = it,
                                    textAlign = TextAlign.Start,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xffffffff)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_email_24),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            viewModel.getHomeDataState.value?.email?.let {
                                Text(
                                    text = it,
                                    textAlign = TextAlign.Start,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    modifier = Modifier.offset(0.dp, -2.dp)
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xffffffff)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_phone_24),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            if (isMobileNumberEditing) {
                                viewModel.getHomeDataState.value?.mobileNumber?.let {
                                    TextField(
                                        value = viewModel.getHomeDataState.value!!.mobileNumber!!,
                                        onValueChange = { newValue ->
                                            viewModel.updateMobileNumber(newValue)
                                        },
                                        textStyle = TextStyle(
                                            fontSize = 16.sp,
                                            color = Color.Black
                                        ),
                                        colors = customTextFieldColors,
                                        modifier = Modifier
                                            .height(50.dp)
                                    )
                                }
                            } else {
                                viewModel.getHomeDataState.value?.mobileNumber?.let {
                                    viewModel.getHomeDataState.value!!.mobileNumber?.let { it1 ->
                                        Text(
                                            text = it1,
                                            textAlign = TextAlign.Start,
                                            fontSize = 16.sp,
                                            color = Color.Black,
                                        )
                                    }
                                }

                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xffffffff)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_lock_24),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.size(3.dp))
                            TextButton(onClick = {
                                viewModel.getHomeDataState.value?.email?.let {
                                    loginModel.resetPassword(
                                        email = it
                                    )
                                }
                            }) {
                                Text(
                                    text = stringResource(R.string.change_password),
                                    textAlign = TextAlign.Start,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xffffffff)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_logout_24),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.size(3.dp))
                            TextButton(onClick = {
                                viewModel.deleteUserName()
                                navController.navigate(NavRoute.LoginScreen.route)
                            }) {
                                Text(
                                    text = stringResource(R.string.logout),
                                    textAlign = TextAlign.Start,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(5.dp))
                    Button(
                        modifier = Modifier
                            .padding(start = 130.dp, end = 60.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        onClick = {
                            isMobileNumberEditing = false
                            isUserNameEditing = false
                        //    isImageUpdating = false

                            viewModel.saveProfileData()
                        },
                        enabled = isMobileNumberEditing || isImageUpdating,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ), shape = RoundedCornerShape(15.dp)
                    ) {
                        androidx.compose.material3.Text(stringResource(R.string.save))
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(top = 180.dp)
                    .height(120.dp)
                    .width(120.dp)
                    .align(Alignment.TopCenter)
                    .offset(x = (7).dp, y = (-50).dp)
                    .clip(RoundedCornerShape(30.dp)),
            ) {
                AsyncImage(
                    model = viewModel.profileImage.value,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Card(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
                    .offset(45.dp, -160.dp),
                border = BorderStroke(1.dp, Color.White),
                colors = CardDefaults.cardColors(containerColor = Color(0xff495d92)),
                shape = CircleShape,
            ) {
                Icon(painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(25.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            scope.launch {
                                isImageUpdating = true
                                modalBottomSheetState.show()
                            }
                        })
            }
            Card(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-55).dp, y = (-210).dp),
                elevation = CardDefaults.cardElevation(15.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp)
                        //.align(Alignment.Center)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            isUserNameEditing = !isUserNameEditing
                            isMobileNumberEditing = !isMobileNumberEditing
                        }
                )
            }
        }
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.setHomeData(profileData)
    }
}

fun <T> NavHostController.setData(key: String, value: T) {
    currentBackStackEntry?.savedStateHandle?.set(key, value)
}

fun <T> NavHostController.getData(key: String): T? {
    return previousBackStackEntry?.savedStateHandle?.get<T>(key)
}



