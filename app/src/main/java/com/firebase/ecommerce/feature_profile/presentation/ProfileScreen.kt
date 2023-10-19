package com.firebase.ecommerce.feature_profile.presentation

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.Constants
import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_home.domain.HomeData
import com.firebase.ecommerce.feature_login.presentation.screens.LoadingButton
import com.firebase.ecommerce.feature_login.presentation.viewmodels.LoginViewModel
import com.firebase.ecommerce.feature_profile.domain.model.ProfileModel
import com.firebase.ecommerce.navigation.NavRoute
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    context: Context,
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
    loginModel: LoginViewModel = hiltViewModel(),
    profileData: HomeData,

    ) {
    var isDialogVisible by remember { mutableStateOf(false) }

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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xfff1e4e0))
                .verticalScroll(rememberScrollState())
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

                        PasswordUpdateDialog(
                            context = context,
                            showDialog = isDialogVisible,
                            onDismiss = { isDialogVisible = false },
                            onUpdatePassword = { newPassword, confirmPassword ->
                                loginModel.updatePassword(newPassword, confirmPassword)
                            }
                        )

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
                                    isDialogVisible = true
                                }
                                ) {
                                    Text(
                                        text = stringResource(R.string.update_password),
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
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                isUserNameEditing = !isUserNameEditing
                                isMobileNumberEditing = !isMobileNumberEditing
                            }
                    )
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordUpdateDialog(
    context: Context,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onCancelButtonClick: () -> Unit = {},
    onUpdatePassword: (newPassword: String, confirmPassword: String) -> Unit,
) {
    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }
    var showConfirmPassword by rememberSaveable {
        mutableStateOf(false)
    }
    var newPassword = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }

    var passwordsMatch by remember { mutableStateOf(true) }

    if (showDialog) {

        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(26.dp)
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(percent = 10)
                        )
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = newPassword.value,
                        onValueChange = { newValue ->
                            newPassword.value = newValue
                            passwordsMatch = newValue == confirmPassword.value
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_lock_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .clickable {
                                        showPassword = !showPassword
                                    }
                                    .size(dimensionResource(id = R.dimen.twenty)),
                                tint = Color.Black.copy(alpha = 0.5f)
                            )
                        },
                        supportingText = {
                            if (newPassword.value.length < 6 && newPassword.value.isNotEmpty()) {
                                androidx.compose.material3.Text(
                                    stringResource(R.string.mininmum_length_of_password_is_6),
                                    color = Color.Red
                                )
                            }

                        },
                        placeholder = { Text(text = stringResource(R.string.new_password)) },
                        trailingIcon = {
                            if (showPassword) {
                                Icon(
                                    painter = painterResource(id = R.drawable.hide_password),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .clickable {
                                            showPassword = !showPassword
                                        }
                                        .size(dimensionResource(id = R.dimen.twentyFive)),
                                    tint = Color.Black.copy(alpha = 0.5f)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.password),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .clickable {
                                            showPassword = !showPassword
                                        }
                                        .size(dimensionResource(id = R.dimen.twentyFive)),
                                    tint = Color.Black.copy(alpha = 0.5f)
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                        ),
                        visualTransformation =
                        if (!showPassword) {
                            PasswordVisualTransformation()
                        } else VisualTransformation.None,
                        maxLines = 1,
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = if (newPassword.value.length < 6) Color.Red else Color.LightGray
                        ),

                        )

                    OutlinedTextField(
                        value = confirmPassword.value,
                        onValueChange = { newValue ->
                            confirmPassword.value = newValue
                            passwordsMatch = newValue == newPassword.value
                        },
                        supportingText = {
                            if (newPassword.value != confirmPassword.value && confirmPassword.value.isNotEmpty()) {
                                androidx.compose.material3.Text(
                                    text = stringResource(id = R.string.passwordMismatch),
                                    color = Color.Red
                                )
                            }
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = if (newPassword != confirmPassword) Color.Red else Color.LightGray
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_lock_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .size(dimensionResource(id = R.dimen.twenty)),
                                tint = Color.Black.copy(alpha = 0.5f)
                            )
                        },
                        placeholder = { Text(text = stringResource(R.string.confirm_password)) },
                        trailingIcon = {
                            if (showConfirmPassword) {
                                Icon(
                                    painter = painterResource(id = R.drawable.hide_password),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .clickable {
                                            showConfirmPassword = !showConfirmPassword
                                        }
                                        .size(dimensionResource(id = R.dimen.twentyFive)),
                                    tint = Color.Black.copy(alpha = 0.5f)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.password),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .clickable {
                                            showConfirmPassword = !showConfirmPassword
                                        }
                                        .size(dimensionResource(id = R.dimen.twentyFive)),
                                    tint = Color.Black.copy(alpha = 0.5f)
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = true,
                            keyboardType = KeyboardType.Password,
                        ),
                        visualTransformation =
                        if (!showConfirmPassword) {
                            PasswordVisualTransformation()
                        } else VisualTransformation.None,
                        maxLines = 1,
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            if (passwordsMatch) {
                                onUpdatePassword(newPassword.value, confirmPassword.value)
                                Toast.makeText(
                                    context,
                                    "Password updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onDismiss()
                            }
                        },
                        enabled = newPassword.value.isNotEmpty() && confirmPassword.value.isNotEmpty() && passwordsMatch
                    ) {
                        Text(
                            text = stringResource(R.string.update),
                            modifier = Modifier.align(Alignment.CenterVertically),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Icon(
                    painter = painterResource(id = R.drawable.cancelbutton),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                        .border(
                            width = dimensionResource(id = R.dimen.two),
                            shape = CircleShape,
                            color = Color.Black
                        )
                        .size(dimensionResource(id = R.dimen.twentyTwo))
                        .align(
                            alignment = Alignment.TopCenter
                        )
                        .clickable {
                            onCancelButtonClick.invoke()
                            onDismiss()
                        }
                )
            }
        }
    }
}



