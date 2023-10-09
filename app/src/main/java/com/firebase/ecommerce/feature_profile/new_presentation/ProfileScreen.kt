package com.firebase.ecommerce.feature_profile.new_presentation

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.Constants
import com.firebase.ecommerce.feature_profile.domain.model.ProfileModel
import com.firebase.ecommerce.feature_profile.presentation.profileViewModel.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(context: Context, viewModel: ProfileViewModel = hiltViewModel()) {

    var userName by remember { mutableStateOf("John Richard") }
    var isUserNameEditing by remember { mutableStateOf(false) }

    var mobileNumber by remember { mutableStateOf("+34 456 321 8720") }
    var isMobileNumberEditing by remember { mutableStateOf(false) }

    val email by remember { mutableStateOf("ankita@gmail.com") }
    val gender by remember { mutableStateOf("Female") }

    val customTextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.Transparent
    )
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

    val scope = rememberCoroutineScope()

    val profileModel = ProfileModel(
        email = email,
        mobileNumber = mobileNumber,
        gender = gender,
        userName = userName,
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xfff1e4e0)) // Background color for the entire box
            .padding(
                start = 4.dp,
                top = 4.dp,
                end = 4.dp,
            )
        // .verticalScroll(rememberScrollState())
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
                    androidx.compose.material3.Text("Choose from Gallery")
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
                    androidx.compose.material3.Text("Choose from Camera")
                }
            }
        }
    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
              //  .clip(shape = RoundedCornerShape(topStart = 50.dp, topEnd = 30.dp))
                .background(
                    color = Color(0xfffc7a5a),/* shape = RoundedCornerShape(25.dp)*/
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
                        .padding(start = 25.dp, end = 25.dp, top = 70.dp)
                ) {
                    if (isUserNameEditing) {
                        TextField(
                            value = userName,
                            onValueChange = { newValue ->
                                userName = newValue
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
                    } else {
                        Text(
                            text = userName,
                            fontSize = 25.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .height(60.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.size(50.dp))

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
                            Text(
                                text = gender,
                                textAlign = TextAlign.Start,
                                fontSize = 16.sp,
                                color = Color.Black,
                            )
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
                            Text(
                                text = email,
                                textAlign = TextAlign.Start,
                                fontSize = 16.sp,
                                color = Color.Black,
                                modifier = Modifier.offset(0.dp, -2.dp)
                            )
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
                                TextField(
                                    value = mobileNumber,
                                    onValueChange = { newValue ->
                                        mobileNumber = newValue
                                    },
                                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                                    colors = customTextFieldColors,
                                    modifier = Modifier
                                        .height(50.dp)
                                )
                            } else {
                                Text(
                                    text = mobileNumber,
                                    textAlign = TextAlign.Start,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                  //  modifier = Modifier.height(50.dp)
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
                                painter = painterResource(id = R.drawable.baseline_lock_24),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(
                                text = "Change Password",
                                textAlign = TextAlign.Start,
                                fontSize = 16.sp,
                                color = Color.Black,
                            )
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
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(
                                text = "Logout",
                                textAlign = TextAlign.Start,
                                fontSize = 16.sp,
                                color = Color.Black,
                            )
                        }
                    }/*  Button(
                          modifier = Modifier
                              .padding(start = 130.dp, end = 60.dp)
                              .clip(RoundedCornerShape(8.dp)),
                          onClick = {
                              isMobileNumberEditing = false
                              isUserNameEditing = false
                              //   viewModel.saveUserDetails(profileModel,context)
                          },
                          enabled = isMobileNumberEditing,
                          colors = ButtonDefaults.buttonColors(
                              containerColor = MaterialTheme.colorScheme.primary
                          ), shape = RoundedCornerShape(15.dp)
                      ) {
                          androidx.compose.material3.Text("Save", modifier = Modifier.padding(10.dp))
                      }
  */
                }
            }
            Box(
                modifier = Modifier
                    .padding(top = 180.dp)
                    .height(100.dp)
                    .width(100.dp)
                    .align(Alignment.TopCenter)
                    .offset(x = (7).dp, y = (-40).dp)
                    .clip(RoundedCornerShape(30.dp)),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Card(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
                    .offset(40.dp, -200.dp),
                border = BorderStroke(1.dp, Color.White),
                colors = CardDefaults.cardColors(containerColor = Color(0xfffc7a5a)),
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
                                modalBottomSheetState.show()
                            }
                        })
            }
            Card(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-55).dp, y = (-235).dp),
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
}


