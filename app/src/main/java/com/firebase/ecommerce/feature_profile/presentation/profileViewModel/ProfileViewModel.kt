package com.firebase.ecommerce.feature_profile.presentation.profileViewModel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ecommerce.core.APIResponse
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_profile.domain.model.ProfileModel
import com.firebase.ecommerce.feature_profile.domain.use_cases.AddImageToStorageUseCase
import com.firebase.ecommerce.feature_profile.domain.use_cases.AddUrlToFirestoreUsecase
import com.firebase.ecommerce.feature_profile.domain.use_cases.GetImageFromFirestoreUsecase
import com.firebase.ecommerce.feature_profile.domain.use_cases.SaveUserDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val addImageToStorageUseCase: AddImageToStorageUseCase,
    private val addUrlToFirestoreUsecase: AddUrlToFirestoreUsecase,
    private val getImageFromFirestoreUsecase: GetImageFromFirestoreUsecase,
    private val saveUserDetailsUseCase: SaveUserDetailsUseCase
) : ViewModel() {

    private val _saveImageInFireStore: MutableState<APIResponse<Boolean?>> =
        mutableStateOf(APIResponse())
    val saveImageInFireStore: State<APIResponse<Boolean?>> = _saveImageInFireStore

    private val _getImageUrl: MutableState<APIResponse<String?>> = mutableStateOf(APIResponse())
    val getImageUrl: State<APIResponse<String?>> = _getImageUrl

    init {
        getImageUrlFromDatabase()
    }


    fun addImageUriToStorage(imageUri: Uri) {
        viewModelScope.launch {
            addImageToStorageUseCase.invoke(imageUri).onEach { resource ->
                when (resource) {
                    is Resource.Success -> {
                        addImageUriToDatabase(imageUri)
                        //    Toast.makeText(context,"Image uploaded successfully",Toast.LENGTH_SHORT).show()

                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }
                }
            }.launchIn(this)
        }
    }


    fun addImageUriToDatabase(downloadUrl: Uri) {
        viewModelScope.launch {
            addUrlToFirestoreUsecase.invoke(downloadUrl).onEach {
                when (it) {
                    is Resource.Success -> {
                        getImageUrlFromDatabase()
                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }
                }
            }.launchIn(this)
        }
    }


    fun getImageUrlFromDatabase() {
        viewModelScope.launch {
            getImageFromFirestoreUsecase.invoke().onEach {
                when (it) {
                    is Resource.Success -> {
                        _getImageUrl.value =
                            _getImageUrl.value.copy(isLoading = false, response = it.data)
                    }

                    is Resource.Loading -> {
                        _getImageUrl.value = _getImageUrl.value.copy(isLoading = true)
                    }

                    is Resource.Error -> {
                        _getImageUrl.value = _getImageUrl.value.copy(
                            isLoading = false,
                            errorMessage = it.message.orEmpty()
                        )
                    }
                }
            }.launchIn(this)
        }
    }
    fun saveUserDetails(profileModel: ProfileModel, context: Context) {
        viewModelScope.launch {
            saveUserDetailsUseCase.saveUserDetails(context = context, profileModel = profileModel)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                           // _signInState.send(SignInState(isSuccess = context.getString(R.string.successfullyLoggedIn)))
                        }
                        is Resource.Loading -> {
                          //  _signInState.send(SignInState(isLoading = true))
                        }
                        is Resource.Error -> {
                           // _signInState.send(SignInState(isError = result.message))
                        }
                    }
                }
        }
    }
}