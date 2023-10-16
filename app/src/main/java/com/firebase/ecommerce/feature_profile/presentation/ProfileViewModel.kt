package com.firebase.ecommerce.feature_profile.presentation

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_home.domain.HomeData
import com.firebase.ecommerce.feature_profile.domain.model.ProfileModel
import com.firebase.ecommerce.feature_profile.domain.use_cases.AddImageToStorageUseCase
import com.firebase.ecommerce.feature_profile.domain.use_cases.GetImageFromFirestoreUsecase
import com.firebase.ecommerce.feature_profile.domain.use_cases.SaveUserDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val addImageToStorageUseCase: AddImageToStorageUseCase,
    private val getImageFromFirestoreUsecase: GetImageFromFirestoreUsecase,
    private val saveUserDetailsUseCase: SaveUserDetailsUseCase,
    private val application: Application,
    private val dataStore: StoreData,
) : ViewModel() {

    private val _getHomeDataState: MutableState<HomeData?> = mutableStateOf(null)
    val getHomeDataState: State<HomeData?> = _getHomeDataState

    private val _profileImage: MutableState<Any?> = mutableStateOf(null)
    val profileImage: State<Any?> = _profileImage

    fun updateMobileNumber(number: String) {
        _getHomeDataState.value = _getHomeDataState.value?.copy(mobileNumber = number)
    }

    fun updateUsername(name: String) {
        _getHomeDataState.value = _getHomeDataState.value?.copy(userName = name)
    }

    fun setHomeData(homeData: HomeData) {
        _getHomeDataState.value = homeData
        _profileImage.value = homeData.image
    }

    fun setImage(imageUri: Uri) {
        _profileImage.value = imageUri
    }

    fun setImage(bitmap: Bitmap) {
        val uri = toBitmapImageToUri(application, bitmap)
        _profileImage.value = uri
    }

    fun saveProfileData() {
        var isProfileImageUpdate: Boolean = false

        if (_profileImage.value is Uri) {
            isProfileImageUpdate = true
        }

        if (_profileImage.value is String) {
            isProfileImageUpdate = false
        }

        if (isProfileImageUpdate) {
            addImageUriToFirestore(_profileImage.value as Uri)
        } else {
            saveUserDetails(_profileImage.value as String)
        }
    }

    fun addImageUriToFirestore(imageUri: Uri) {
        viewModelScope.launch {
            addImageToStorageUseCase.invoke(imageUri).onEach {
                when (it) {
                    is Resource.Success -> {
                        getImageUrlFromDatabase()
                    }

                    is Resource.Error -> Unit

                    is Resource.Loading -> Unit
                }
            }.launchIn(this)
        }
    }


    fun getImageUrlFromDatabase() {
        viewModelScope.launch {
            getImageFromFirestoreUsecase.invoke().onEach {
                when (it) {
                    is Resource.Success -> {
                        saveUserDetails(it.data!!)
                    }

                    is Resource.Loading -> Unit

                    is Resource.Error -> Unit
                }
            }.launchIn(this)
        }
    }

    fun saveUserDetails(imageUrl: String) {
        viewModelScope.launch {
            val profileModel =
                ProfileModel(
                    email = getHomeDataState.value!!.email,
                    mobileNumber = getHomeDataState.value!!.mobileNumber,
                    gender = getHomeDataState.value!!.gender,
                    userName = getHomeDataState.value!!.userName!!,
                    image = imageUrl
                )

            saveUserDetailsUseCase.invoke(profileModel = profileModel)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            Toast.makeText(application, result.data.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }

                        is Resource.Loading -> Unit

                        is Resource.Error -> {
                            Toast.makeText(application, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }

    fun toBitmapImageToUri(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver, bitmap, "Title", null
        )
        return Uri.parse(path)
    }

    fun deleteUserName() {
        viewModelScope.launch {
            dataStore.saveData("")
        }
    }
}

