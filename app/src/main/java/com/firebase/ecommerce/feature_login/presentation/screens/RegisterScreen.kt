package com.firebase.ecommerce.feature_login.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.firebase.ecommerce.R
import com.firebase.ecommerce.feature_login.domain.model.RegistrationDetails
import com.firebase.ecommerce.feature_login.presentation.viewmodels.RegistrationViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(viewModel: RegistrationViewModel = hiltViewModel()) {
    var emailId by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var confirmPassword by rememberSaveable {
        mutableStateOf("")
    }
    var mobileNumber by rememberSaveable {
        mutableStateOf("")
    }
    var gender by rememberSaveable {
        mutableStateOf("")
    }
    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }
    var showConfirmPassword by rememberSaveable {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    var errorMessage by remember {
        mutableStateOf("")
    }
    val context= LocalContext.current

    val data = viewModel.signInState.collectAsState(initial = null)
    val registrationDetails = RegistrationDetails(
        email = emailId,
        password = password,
        mobileNumber = mobileNumber,
        gender = gender

    )
    val validateEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.Register),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp
        )
        Text(
            text = stringResource(id = R.string.registrationSubTitle),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id =  R . dimen . thirty)),
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal,
            color = Color.Gray
        )
        Text(
            text = stringResource(id = R.string.Email),
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id =  R . dimen . forty),
                    top = dimensionResource(id =  R . dimen . ten),
                    bottom = dimensionResource(id =  R . dimen . five)
                ),
            fontWeight = FontWeight.SemiBold
        )
        OutlinedTextField(
            value = emailId,
            onValueChange = {
                emailId = it
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_email_24),
                    contentDescription = null,
                    tint = Color.Gray.copy(alpha = 1f)
                )
            },
            supportingText = {
                if (!validateEmail && emailId.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.emailWarning),
                        color = Color.Red
                    )
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(id =  R . dimen . forty), end = dimensionResource(id =  R . dimen . forty)),
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
        Text(
            text = stringResource(id = R.string.Password),
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(id =  R . dimen . forty), top = dimensionResource(id =  R . dimen . ten), bottom = dimensionResource(id =  R . dimen . five)),
            fontWeight = FontWeight.SemiBold
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_lock_24),
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentSize()
                        .size(dimensionResource(id =  R . dimen . twenty)),
                    tint = Color.Black.copy(alpha = 0.5f)
                )
            },
            supportingText = {
                if (password.length < 6 && password.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.passwordValidation),
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
                            .size(dimensionResource(id =  R . dimen . twentyFive)),
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
                            .size(dimensionResource(id =  R . dimen . twentyFive)),
                        tint = Color.Black.copy(alpha = 0.5f)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(id =  R . dimen . forty), end = dimensionResource(id =  R . dimen . forty)),
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
                focusedBorderColor = if (password.length < 6) Color.Red else Color.LightGray
            ),

            )
        Text(
            text = stringResource(id = R.string.ConfirmPassword),
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(id =  R . dimen . forty), top = dimensionResource(id =  R . dimen . ten), bottom = dimensionResource(id =  R . dimen . five)),
            fontWeight = FontWeight.SemiBold
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
            },
            supportingText = {
                if (password != confirmPassword && confirmPassword.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.passwordMismatch),
                        color = Color.Red
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (password != confirmPassword) Color.Red else Color.LightGray
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_lock_24),
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentSize()
                        .size(dimensionResource(id =  R . dimen . twenty)),
                    tint = Color.Black.copy(alpha = 0.5f)
                )
            },
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
                            .size(dimensionResource(id =  R . dimen . twentyFive)),
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
                            .size(dimensionResource(id =  R . dimen . twentyFive)),
                        tint = Color.Black.copy(alpha = 0.5f)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(id =  R . dimen . forty), end = dimensionResource(id =  R . dimen . forty)),
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
        Text(
            text = stringResource(id = R.string.MobileNumber),
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(id =  R . dimen . forty), top = dimensionResource(id =  R . dimen . ten), bottom = dimensionResource(id =  R . dimen . five)),
            fontWeight = FontWeight.SemiBold
        )
        OutlinedTextField(
            value = mobileNumber,
            onValueChange = {
                mobileNumber = it
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_phone_24),
                    contentDescription = null,
                    tint = Color.Gray.copy(alpha = 1f)
                )
            },
            supportingText = {
                if (mobileNumber.length < 10 && mobileNumber.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.mobileNumberValidation),
                        color = Color.Red
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(id =  R . dimen . forty), end = dimensionResource(id =  R . dimen . forty)),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = true,
                keyboardType = KeyboardType.Phone,
            ),
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (mobileNumber.length < 10) Color.Red else Color.LightGray
            ),
        )

        Text(
            text = stringResource(id = R.string.Gender),
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(id =  R . dimen . forty), top = dimensionResource(id =  R . dimen . ten), bottom = dimensionResource(id =  R . dimen . five)),
            fontWeight = FontWeight.SemiBold
        )
        Row(modifier = Modifier.padding(start = dimensionResource(id =  R . dimen . forty))) {
            SimpleRadioButtonComponent(
                onSelectedOption = {
                    gender = it
                })
        }
        Button(
            shape = RoundedCornerShape(dimensionResource(id =  R . dimen . fifteen)),
            onClick = {
                viewModel.storeRegistrationDetailsWithAuthentication(registrationDetails,context)

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id =  R . dimen . thirty), start = dimensionResource(id =  R . dimen . thirty), end = dimensionResource(id =  R . dimen . thirty)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xff34495E)

            ),
            enabled = emailId.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && mobileNumber.isNotEmpty() && (password.length == 6 || password.length > 6) && validateEmail
        ) {
            Text(
                text = stringResource(id = R.string.SignUp),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(dimensionResource(id =  R . dimen . eight)),
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )

        }
        if (errorMessage.isNotEmpty()) {
            CustomDialogBox(
                showDialog = true,
                message = errorMessage,
                onCancelButtonClick = {
                    errorMessage = ""
                })
        }

        LaunchedEffect(key1 = data.value?.isSuccess, block = {
            scope.launch {
                if (data.value?.isSuccess?.isNotEmpty() == true) {
                    errorMessage = "successfully logged in"
                }
            }
        })

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(id = R.string.AlreadyHaveAnAccount))
            TextButton(
                onClick = {

                },
            ) {
                Text(
                    text = stringResource(id = R.string.SignIn),
                    color = Color(0xff34495E)
                )
            }

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (data.value?.isLoading == true) {
                CircularProgressIndicator()
            }
        }
        LaunchedEffect(key1 = data.value?.isError?.isNotEmpty() == true, block = {
            scope.launch {
                if (data.value?.isError?.isNotEmpty() == true) {
                    errorMessage = data.value!!.isError!!
                }
            }

        })
    }


}

@Composable
fun SimpleRadioButtonComponent(onSelectedOption: (String) -> Unit = {}) {
    val radioOptions =
        listOf(stringResource(id = R.string.male), stringResource(id = R.string.female))
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    onSelectedOption(selectedOption)
    radioOptions.forEach { text ->
        Row(
            modifier = Modifier
                .wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (text == selectedOption),
                modifier = Modifier,
                onClick = {
                    onOptionSelected(text)
                }
            )
            Text(
                text = text,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun CustomDialogBox(
    showDialog: Boolean = false,
    message: String,
    onCancelButtonClick: () -> Unit = {}
) {

    var dialogOpen by remember {
        mutableStateOf(showDialog)
    }

    if (dialogOpen) {
        Dialog(onDismissRequest = {
            dialogOpen = false
        }
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = dimensionResource(id =  R . dimen . thirty))
                            .fillMaxWidth()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(percent = 10)
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(height = dimensionResource(id =  R . dimen . thirtySix)))
                        Text(
                            modifier = Modifier.padding(horizontal = dimensionResource(id =  R . dimen . sixteen)),
                            text = message,
                            fontSize = 18.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            TextButton(onClick = {
                                onCancelButtonClick.invoke()
                                dialogOpen = false
                            }) {
                                Text(text = stringResource(id = R.string.ok), fontSize = 20.sp)
                            }
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
                                width = dimensionResource(id =  R . dimen . two),
                                shape = CircleShape,
                                color = Color.Black
                            )
                            .size(dimensionResource(id =  R . dimen . twentyTwo))
                            .align(
                                alignment = Alignment.TopCenter
                            )
                            .clickable {
                                onCancelButtonClick.invoke()
                                dialogOpen = false
                            }
                    )
                }
            }
        }
    }
}






