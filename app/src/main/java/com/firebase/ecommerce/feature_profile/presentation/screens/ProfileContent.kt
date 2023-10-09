/*
package com.firebase.ecommerce.feature_profile.presentation.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.firebase.ecommerce.R
import com.firebase.ecommerce.feature_profile.presentation.GetImageFromDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    openGallery: () -> Unit,
) {


    var userName by remember { mutableStateOf("Summer Doe") }
    var isUserNameEditing by remember { mutableStateOf(false) }

    var mobileNumber by remember { mutableStateOf("+34 456 321 8720") }
    var isMobileNumberEditing by remember { mutableStateOf(false) }

    val customTextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.Transparent
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "My Profile",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 115.dp),
            color = Color(0xff5c4373)
        )
        // Profile Picture
        Box(
            modifier = Modifier
                .size(250.dp)
                .background(color = Color.Yellow, shape = CircleShape)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .height(250.dp),
                contentScale = ContentScale.FillBounds,
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        openGallery.invoke()
                    }
                    .align(Alignment.BottomEnd)
                    .offset(x = (-20).dp, y = (-10).dp)
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        if (isUserNameEditing) {
            TextField(
                value = userName,
                onValueChange = { newValue ->
                    userName = newValue
                },
                textStyle = TextStyle(fontSize = 20.sp, color = Color(0xff735b85)),
                colors = customTextFieldColors,
                modifier = Modifier.padding(start = 10.dp)
            )
        } else {
            Text(
                text = userName,
                color = Color(0xff382656),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
//                        .align(Alignment.Center)
                    .clickable {
                        isUserNameEditing = true
                    },
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold
            )
        }



        Spacer(modifier = Modifier.height(60.dp))

        Row() {
            Icon(
                painter = painterResource(id = R.drawable.baseline_female_24),
                contentDescription = "",
                modifier = Modifier.padding(start = 10.dp)
            )
            Text(
                text = "Gender",
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Text(
            text = "Female",
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 12.dp),
            color = Color(0xff735b85)
        )

        Spacer(modifier = Modifier.size(20.dp))
        Row() {
            Icon(
                painter = painterResource(id = R.drawable.baseline_phone_24),
                contentDescription = "",
                modifier = Modifier.padding(start = 10.dp)
            )
            Text(
                text = "Mobile",
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 10.dp)
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
                modifier = Modifier.padding(start = 10.dp)
            )
        } else {
            // Display the text when not in edit mode
            Text(
                text = mobileNumber,
                color = Color(0xff735b85),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable {
                        isMobileNumberEditing = true
                    }
            )
        }


        Spacer(modifier = Modifier.size(20.dp))
        Row() {
            Icon(
                painter = painterResource(id = R.drawable.baseline_email_24),
                contentDescription = "",
                modifier = Modifier.padding(start = 10.dp)
            )
            Text(
                text = "Email",
                color = Color.DarkGray,
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Text(
            text = "ankita@gmail.com",
            fontSize = 20.sp,
            color = Color(0xff735b85),
            modifier = Modifier.padding(start = 10.dp)
        )
        Spacer(modifier = Modifier.size(30.dp))
        Row(
            Modifier
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    isUserNameEditing = !isUserNameEditing
                    isMobileNumberEditing = !isMobileNumberEditing
                },
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text("Edit")
            }
            Button(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clip(RoundedCornerShape(8.dp)),
                onClick = {
                    isMobileNumberEditing = false
                    isUserNameEditing = false
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ), shape = RoundedCornerShape(5.dp)
            ) {
                Text("Save")
            }
        }
    }
   */
/* GetImageFromDatabase(
        createProfileImageContent = { imageUrl ->
            ProfileImageContent(imageUrl)
        }
    )*//*


}*/
