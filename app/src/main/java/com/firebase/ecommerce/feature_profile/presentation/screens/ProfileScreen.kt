/*
package com.firebase.ecommerce.feature_profile.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.firebase.ecommerce.core.Constants.ALL_IMAGES
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.firebase.ecommerce.feature_profile.presentation.AddImageToDatabase
import com.firebase.ecommerce.feature_profile.presentation.AddImageToStorage
import com.firebase.ecommerce.feature_profile.presentation.profileViewModel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileImageScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val galleryLauncher =  rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
        imageUri?.let{
            viewModel.addImageUriToStorage(imageUri)
        }
    }

    Scaffold(
        content = { padding ->
            Box(
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                ProfileContent(
                    openGallery = {
                        galleryLauncher.launch(ALL_IMAGES)
                    }
                )
            }
        },
       scaffoldState = scaffoldState
    )

    AddImageToStorage(
        addImageToDatabase = { downloadUrl ->
            viewModel.addImageUriToDatabase(downloadUrl)
        }
    )

    fun showSnackBar() = coroutineScope.launch {
        scaffoldState.snackbarHostState.showSnackbar(
            message = "Image Updated Successfully",
         //   actionLabel = DISPLAY_IT_MESSAGE
        )

    }

AddImageToDatabase(
        showSnackBar = { isImageAddedToDatabase ->
            if (isImageAddedToDatabase) {
                showSnackBar()
            }
        }
    )

    AddImageToDatabase {
        showSnackBar()
        viewModel.getImageUrlFromDatabase()
    }
}
*/
