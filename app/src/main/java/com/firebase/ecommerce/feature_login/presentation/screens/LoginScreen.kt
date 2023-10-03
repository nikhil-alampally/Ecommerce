package com.firebase.ecommerce.feature_login.presentation.screens

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.ConnectionState
import com.firebase.ecommerce.core.connectivityState
import com.firebase.ecommerce.feature_home.data.HomeDataDto
import com.firebase.ecommerce.feature_login.presentation.viewmodels.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navigate: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit
) {
    val connection by connectivityState()
    if (connection == ConnectionState.Unavailable) {
        var showDialog by remember {
            mutableStateOf(true)
        }
        CustomDialogBox(
            message = stringResource(id = R.string.NoInternet),
            onCancelButtonClick = { showDialog = true },
            showDialog = showDialog
        )
    }


    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signInState.collectAsState(initial = null)
    val googleState = viewModel.googleSignInState.collectAsState(initial = null)
    val googleData = viewModel.googleSignDataState.collectAsState(initial = null)
    val validateEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }
    var errorMessage by remember {
        mutableStateOf("")
    }
    var url by rememberSaveable {
        mutableStateOf("")
    }
    var googleDataForHomePage: HomeDataDto? by remember {
        mutableStateOf(null)
    }
    val token = stringResource(R.string.default_web_client_id)
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                viewModel.signWithGoogle(credential)
                val registrationDetails = HomeDataDto(
                    userName = account.displayName.toString(),
                    image = account.photoUrl.toString(),
                    email = account.email.toString(),
                )
                googleDataForHomePage = registrationDetails
            } catch (e: ApiException) {
                errorMessage = e.localizedMessage as String
            }
        }
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(token)
        .requestEmail()
        .build()
    googleDataForHomePage?.let { viewModel.storingGoogleSignInDataIntoFireStore(it,context) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.sixteen))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login),
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.fiftyFive))
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp
        )

        Text(
            text = stringResource(R.string.access_account),
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.twenty)))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                modifier = Modifier.size(dimensionResource(id = R.dimen.oneTwentyEight)),
                onClick = {}
            ) {
                Image(
                    painter = painterResource(R.drawable.fb),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
            LaunchedEffect(key1 = url, block = {
                scope.launch {
                    viewModel.getUserID().collect {
                        if (it != null) {
                            url = it
                        }
                    }

                }
                if (url.isNotEmpty()) {
                    navigateToHomeScreen.invoke()
                }
            })

            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.twenty)))
            IconButton(
                onClick = {
                    if (url.isEmpty()) {
                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        launcher.launch(googleSignInClient.signInIntent)
                    } else {
                        navigateToHomeScreen.invoke()
                    }

                },
                modifier = Modifier.size(dimensionResource(id = R.dimen.oneTwentyEight)),
            ) {
                Image(
                    painter = painterResource(R.drawable.google),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Text(
            text = stringResource(R.string.or_login_with_email),
            fontWeight = FontWeight.SemiBold
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.twenty))
        ) {
            Text(
                text = stringResource(R.string.email),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.ten)))

            OutlinedTextField(
                value = email.value,
                onValueChange = {
                    email.value = it
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_email_24),
                        contentDescription = null, tint = Color.Gray.copy(alpha = 1f)
                    )
                },
                supportingText = {
                    if (!validateEmail && email.value.isNotEmpty()) {
                        Text(stringResource(id = R.string.emailWarning), color = Color.Red)
                    }

                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Email,
                ),
                maxLines = 1,
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (!validateEmail) Color.Red else Color.LightGray

                ),
            )
            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.twenty)))
            Text(
                text = stringResource(R.string.password),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.ten)))

            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    password.value = it
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
                    if (password.value.length < 6 && password.value.isNotEmpty()) {
                        Text(
                            stringResource(R.string.mininmum_length_of_password_is_6),
                            color = Color.Red
                        )
                    }

                },
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
                    .fillMaxWidth(),
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
                    focusedBorderColor = if (password.value.length < 6) Color.Red else Color.LightGray
                ),

                )
            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.sixty)))
            LoadingButton(
                onClick = {

                    viewModel.loginUser(email.value, password.value)
                    if (state.value?.isSuccess?.isNotEmpty() == true) {
                        navigateToHomeScreen.invoke()
                    }

                },
                textComposable = {
                    Text(
                        text = stringResource(id = R.string.SignIn),
                        modifier = Modifier
                            .weight(1f)
                            .offset((-12).dp)
                            .wrapContentSize()
                            .padding(dimensionResource(id = R.dimen.eight)),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                enabled = email.value.isNotEmpty() && password.value.isNotEmpty(),
                onStateChange = state.value?.isLoading == true
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.ten)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(R.string.don_t_have_an_account))
                TextButton(
                    onClick = { navigate.invoke() }
                ) {
                    Text(
                        text = stringResource(R.string.register),
                        color = Color(0xff34495E)
                    )
                }
            }
            if (errorMessage.isNotEmpty()) {
                var showDialog by remember {
                    mutableStateOf(true)
                }
                CustomDialogBox(
                    showDialog = showDialog,
                    message = errorMessage,
                    onCancelButtonClick = {
                        errorMessage = ""
                        showDialog = false
                    })

            }
            LaunchedEffect(key1 = googleState.value?.isSuccess?.isNotEmpty() == true, block = {
                scope.launch {
                    if (googleState.value?.isSuccess?.isNotEmpty() == true) {
                        viewModel.saveUserName(googleState.value?.isSuccess!!)
                    }
                }
            })

            LaunchedEffect(key1 = state.value?.isSuccess?.isNotEmpty() == true) {
                scope.launch {
                    if (state.value?.isSuccess?.isNotEmpty() == true) {
                        viewModel.saveUserName(state.value?.isSuccess!!)
                    }
                }

            }
            LaunchedEffect(
                key1 = state.value?.isError,
                key2 = googleState.value?.isError,
                key3 = googleData.value?.isError
            ) {
                scope.launch {
                    if (state.value?.isError?.isNotEmpty() == true) {
                        errorMessage = state.value!!.isError!!
                    }
                    if (googleState.value?.isError?.isNotEmpty() == true) {
                        errorMessage = googleState.value!!.isError!!
                    }
                    if (googleData.value?.isError?.isNotEmpty() == true) {
                        errorMessage = googleData.value!!.isError!!
                    }
                }
            }
        }
    }
}
        

@Composable
fun LoadingButton(
    onClick: () -> Unit,
    onStateChange: Boolean = false,
    textComposable: @Composable () -> Unit,
    enabled: Boolean = false,
) {
    var showProgress by remember { mutableStateOf(false) }
    Button(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.fifteen)),
        onClick = {
            if (showProgress.not()) {
                onClick.invoke()
            }
            showProgress = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.thirty),
                start = dimensionResource(id = R.dimen.thirty),
                end = dimensionResource(id = R.dimen.thirty)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff34495E)
        ),
        interactionSource = if (showProgress) remember { NoRippleInteractionSource() } else remember { MutableInteractionSource() },
        enabled = enabled
    ) {
        Box(modifier = Modifier.width(20.dp), contentAlignment = Alignment.Center) {
            if (onStateChange) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.Yellow
                )
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
        textComposable.invoke()
    }
    if (showProgress) {
        LaunchedEffect(showProgress) {
            delay(3000)
            showProgress = false
        }
    }
}

class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true
}

