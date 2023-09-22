package com.firebase.ecommerce.feature_login.presentation.screens

import android.widget.Toast
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
import androidx.compose.material3.MaterialTheme
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
import com.firebase.ecommerce.feature_login.presentation.viewmodels.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navigate: () -> Unit, viewModel: LoginViewModel = hiltViewModel()) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signInState.collectAsState(initial = null)
    val validateEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }
    var showProgress by remember { mutableStateOf(false) }
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
            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.twenty)))
            IconButton(
                onClick = {},
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
        Text(text = stringResource(R.string.or_login_with_email), fontWeight = FontWeight.SemiBold)
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
                modifier = Modifier
                    .fillMaxWidth(),
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

            Button(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.fifteen)),
                onClick = {
                    if (showProgress.not()) {
                        viewModel.loginUser(email.value, password.value)
                    }
                    showProgress = true
                    viewModel.loginUser(email.value, password.value)

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
                enabled = email.value.isNotEmpty() && password.value.isNotEmpty()
            ) {
                Box(modifier = Modifier.width(20.dp), contentAlignment = Alignment.Center) {
                    if (state.value?.isLoading == true) {
                      CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.Yellow
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                    }
                }
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

            }
            if (showProgress) {
                LaunchedEffect(showProgress) {
                    delay(3000)
                    showProgress = false
                }
            }
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
            LaunchedEffect(key1 = state.value?.isSuccess) {
                scope.launch {
                    if (state.value?.isSuccess?.isNotEmpty() == true) {
                        val success = state.value?.isSuccess
                        Toast.makeText(context, "${success}", Toast.LENGTH_LONG).show()
                    }
                }
            }
            LaunchedEffect(key1 = state.value?.isError) {
                scope.launch {
                    if (state.value?.isError?.isNotEmpty() == true) {
                        val error = state.value?.isError
                        Toast.makeText(context, "${error}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

class NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true
}
