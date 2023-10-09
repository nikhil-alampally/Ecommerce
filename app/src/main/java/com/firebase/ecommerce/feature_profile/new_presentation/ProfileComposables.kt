package com.firebase.ecommerce.feature_profile.new_presentation

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.Constants
import com.firebase.ecommerce.feature_profile.domain.model.ProfileModel
import com.firebase.ecommerce.feature_profile.presentation.profileViewModel.ProfileViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileComposable(context: Context, viewModel: ProfileViewModel = hiltViewModel()) {
    val scaffoldState = rememberScaffoldState()

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            imageUri?.let {
                viewModel.addImageUriToStorage(imageUri)
            }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview(),
            onResult = {
                if (it != null) {

                    viewModel.addImageUriToStorage(toBitmapImageToUri(context, it))
                }
            })
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
        )


    var userName by remember { mutableStateOf("John Doe") }
    var isUserNameEditing by remember { mutableStateOf(false) }

    var mobileNumber by remember { mutableStateOf("+34 456 321 8720") }
    var isMobileNumberEditing by remember { mutableStateOf(false) }

    val email by remember { mutableStateOf("ankita@gmail.com") }
    val gender by remember { mutableStateOf("Female") }

    val image = "https://firebasestorage.googleapis.com/v0/b/ecommerce-b13bb.appspot.com/o/Profile%2Fcef3845d-2973-40f5-818d-8cde162bc5d1.jpg?alt=media&token=e9ca27eb-b75f-4eb7-bd54-13b996961bfa"

    val customTextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.Transparent
    )
    val profileModel = ProfileModel(
        email = email,
        mobileNumber = mobileNumber,
        gender = gender,
        userName = userName,

    )

    Scaffold(
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

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
                                Text("Choose from Gallery")
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
                                Text("Choose from Camera")
                            }
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                            .verticalScroll(rememberScrollState())
                        //.horizontalScroll(rememberScrollState()),
                    ) {
                        Text(
                            text = "My Profile",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = Color(0xff5c4373)
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        // Profile Picture
                        Box(modifier = Modifier
                            // .size(400.dp)
                            .height(350.dp)
                            .width(400.dp)
                            .background(color = Color(0xfff6f6f6), shape = RectangleShape)
                            .align(Alignment.CenterHorizontally))
                        {
                            Box(
                                modifier = Modifier
                                    // .size(400.dp)
                                    .height(350.dp)
                                    .width(400.dp)
                                    .background(color = Color(0xfff6f6f6), shape = RectangleShape)
                                   // .align(Alignment.CenterHorizontally)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(viewModel.getImageUrl.value.response)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        //.padding(64.dp)
                                        .width(180.dp)
                                        .height(220.dp)
                                        .padding(bottom = 50.dp)
                                        .clip(RoundedCornerShape(15.dp))
                                        .align(Alignment.Center),
                                )

                                if (viewModel.getImageUrl.value.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .align(Alignment.Center)
                                    )
                                }

                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .align(Alignment.CenterEnd)
                                        .offset(x = (-80).dp, y = (40).dp)
                                        .clickable {
                                            scope.launch {
                                                modalBottomSheetState.show()
                                            }
                                        }
                                )
                            //    Spacer(modifier = Modifier.height(30.dp))

                                if (isUserNameEditing) {
                                    TextField(
                                        value = userName,
                                        onValueChange = { newValue ->
                                            userName = newValue
                                        },
                                        textStyle = TextStyle(
                                            fontSize = 40.sp,
                                            color = Color(0xff735b85)
                                        ),
                                        colors = customTextFieldColors,
                                        modifier = Modifier
                                            // .align(Alignment.CenterHorizontally)
                                            .height(100.dp)
                                            .align(Alignment.BottomCenter)

                                    )
                                } else {
                                    Text(
                                        text = userName,
                                        color = Color(0xff382656),
                                        modifier = Modifier
                                            //.align(Alignment.CenterHorizontally)
                                            .height(100.dp)
                                            .align(Alignment.BottomCenter)
                                                ,
                                        fontSize = 40.sp,
                                       // fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Card(modifier = Modifier
                                .size(60.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = (-20).dp, y = (20).dp),
                            elevation = CardDefaults.cardElevation(15.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_edit_24),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(top = 15.dp)
                                        //.align(Alignment.Center)
                                        .align(Alignment.CenterHorizontally)
                                        .clickable {
                                            isUserNameEditing = !isUserNameEditing
                                            isMobileNumberEditing = !isMobileNumberEditing
                                        }
                                )
                            }


                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        Row() {
                            Icon(
                                painter = painterResource(id = R.drawable.img_1),
                                contentDescription = "",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                text = "Gender",
                                textAlign = TextAlign.Start,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xffc4c4c4),
                            )
                        }
                        Text(
                            text = gender,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                           // modifier = Modifier.padding(start = 12.dp),
                            color = Color(0xff735b85)
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                        Row() {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_email_24),
                                contentDescription = "",
                              //  modifier = Modifier.padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                text = "Email",
                                color = Color(0xffc4c4c4),
                                textAlign = TextAlign.Start,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.offset(0.dp,-2.dp)
                             //   modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                        Text(
                            text = email,
                            fontSize = 20.sp,
                            color = Color(0xff735b85),
                            fontWeight = FontWeight.Bold,
                           // modifier = Modifier.padding(start = 10.dp)
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                        Row() {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_phone_24),
                                contentDescription = "",
                             //   modifier = Modifier.padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                text = "Mobile",
                                textAlign = TextAlign.Start,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xffc4c4c4),
                              //  modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                        if (isMobileNumberEditing) {
                            TextField(
                                value = mobileNumber,
                                onValueChange = { newValue ->
                                    mobileNumber = newValue
                                },
                                textStyle = TextStyle(fontSize = 20.sp, color = Color(0xff735b85)),
                                colors = customTextFieldColors,
                                modifier = Modifier
                                    .height(60.dp)
                            )
                        } else {
                            Text(
                                text = mobileNumber,
                                color = Color(0xff735b85),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    // .padding(start = 10.dp)
                                    .height(60.dp)
                            )
                        }

                        Spacer(modifier = Modifier.size(10.dp))
                        Row(
                            Modifier
                                .fillMaxWidth(), horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                onClick = {
                                    isMobileNumberEditing = false
                                    isUserNameEditing = false
                                    viewModel.saveUserDetails(profileModel,context)
                                },
                                enabled = isMobileNumberEditing ,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ), shape = RoundedCornerShape(15.dp)
                            ) {
                                Text("Save", modifier = Modifier.padding(10.dp))
                            }
                        }
                    }
                }

            }
        },
        scaffoldState = scaffoldState
    )
//LaunchedEffect(key1 = , block = )
}

fun toBitmapImageToUri(context: Context, bitmap: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(
        context.contentResolver, bitmap, "Title", null
    )
    return Uri.parse(path)
}
