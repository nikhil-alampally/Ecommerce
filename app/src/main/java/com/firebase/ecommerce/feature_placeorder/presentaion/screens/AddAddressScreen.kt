package com.firebase.ecommerce.feature_placeorder.presentaion.screens

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.Constants
import com.firebase.ecommerce.feature_home.domain.model.HomeData
import com.firebase.ecommerce.feature_home.presentation.screen.setData
import com.firebase.ecommerce.feature_home.presentation.viewmodel.HomeViewModel
import com.firebase.ecommerce.feature_placeorder.data.AddAddress
import com.firebase.ecommerce.feature_placeorder.presentaion.viewmodel.AddAddressViewModel
import com.firebase.ecommerce.feature_placeorder.presentaion.screens.Stepper
import com.firebase.ecommerce.navigation.NavRoute
import kotlinx.coroutines.launch
import java.util.Date


@SuppressLint(
    "CoroutineCreationDuringComposition", "MutableCollectionMutableState",
    "SimpleDateFormat"
)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddAddress(viewModel: AddAddressViewModel = hiltViewModel(), navController: NavHostController) {
    val context = LocalContext.current
    var addressDetails: MutableList<AddAddress>? by remember {
        mutableStateOf(value = mutableListOf())
    }
    var editAddressDetails: String by remember {
        mutableStateOf("")
    }
    var editAddress: AddAddress? by remember {
        mutableStateOf(null)
    }
    var houseNumber by remember {
        mutableStateOf("")
    }
    var addressType by remember {
        mutableStateOf(context.getString(R.string.home))
    }
    var area by remember {
        mutableStateOf("")
    }
    var pinCode by remember {
        mutableStateOf("")
    }
    val isSelectedAddress by remember {
        mutableStateOf(false)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var deletedItem: AddAddress? by remember {
        mutableStateOf(null)
    }
    var selectedState by remember {
        mutableStateOf("")
    }

    if (editAddressDetails.isNotEmpty() && editAddress != null) {
        houseNumber = editAddress!!.houseNumber
        addressType = editAddress!!.addressType
        pinCode = editAddress!!.pincode
        area = editAddress!!.area
    }

    deletedItem?.let {
        addressDetails?.remove(it)
    }

    val scope = rememberCoroutineScope()

    scope.launch {
        viewModel.getDataInState.collect {
            addressDetails = it.isSuccess
        }
        viewModel.getDataInState.collect {
            if (it.isLoading) {
                isLoading = true
            }
        }
    }

    Column {
        val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
        val sheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = sheetState
        )
        BottomSheetScaffold(
            scaffoldState = sheetScaffoldState,
            sheetPeekHeight = 0.dp,
            sheetContent = {
                val mCities = listOf(
                    "Delhi",
                    "Mumbai",
                    "Chennai",
                    "Kolkata",
                    "Hyderabad",
                    "Bengaluru",
                    "Pune"
                )
                var mExpanded by remember { mutableStateOf(false) }
                val icon = if (mExpanded)
                    Icons.Filled.KeyboardArrowUp
                else
                    Icons.Filled.KeyboardArrowDown
                Text(text = stringResource(id = R.string.TypeOfAddress))
                ChipSelectionList(items = listOf(
                    stringResource(id = R.string.home),
                    stringResource(id = R.string.work),
                    stringResource(id = R.string.other)
                ), onItemSelected = {
                    addressType = it
                })

                OutlinedTextField(value = houseNumber, onValueChange = {
                    houseNumber = it
                }, placeholder = {
                    Text(stringResource(id = R.string.houseNumber))
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.ten))
                )
                OutlinedTextField(value = area, onValueChange = {
                    area = it
                }, placeholder = {
                    Text(stringResource(id = R.string.area))
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.ten))
                )
                OutlinedTextField(
                    value = selectedState,
                    onValueChange = { selectedState = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.fifteen)),
                    label = { Text(stringResource(id = R.string.state)) },
                    trailingIcon = {
                        Icon(icon, null,
                            Modifier.clickable { mExpanded = !mExpanded })
                    }
                )

                OutlinedTextField(value = pinCode, onValueChange = {
                    pinCode = it
                }, placeholder = {
                    Text(stringResource(id = R.string.pinCode))
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.ten))
                )
                if (mExpanded) {
                    Dialog(onDismissRequest = {}) {
                        DropdownMenu(
                            expanded = mExpanded,
                            onDismissRequest = { mExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)

                        ) {
                            mCities.forEach { label ->
                                DropdownMenuItem(onClick = {
                                    selectedState = label
                                    mExpanded = false
                                }) {
                                    Text(text = label)
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        val sdf =
                            SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z")
                        val currentDateAndTime = sdf.format(Date())
                        scope.launch { sheetState.collapse() }
                        viewModel.addAddressData(
                            context = context,
                            addAddress = AddAddress(
                                houseNumber = houseNumber,
                                area = area,
                                state = selectedState,
                                pincode = pinCode,
                                addressType = addressType,
                                id = if (editAddressDetails.isEmpty()) currentDateAndTime else editAddressDetails,
                                isSelected = isSelectedAddress,
                            )
                        )
                        scope.launch {
                            viewModel.getData()
                        }

                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.twenty))
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.thirty)))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.ten)))
                    Text(
                        text = stringResource(id = R.string.saveAddress),
                        color = MaterialTheme.colors.primary
                    )
                }

            }) {
            Column {
                Text(
                    text = stringResource(id = R.string.selectAddress),
                    fontSize = 20.sp
                )
                scope.launch {
                    viewModel.addAddressDataState.collect {
                        if (it.isLoading) {
                            isLoading = true
                        }
                        if (it.isSuccess?.isNotEmpty() == true) {
                            isLoading = false
                        }
                    }
                }
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                LazyColumn(content = {
                    addressDetails?.let {
                        items(items = it) { address ->
                            AddAddressCard(
                                addAddress = address,
                                onEditClick = { addressDetails ->
                                    editAddressDetails = addressDetails.id
                                    editAddress = addressDetails
                                    scope.launch {
                                        sheetState.expand()
                                    }
                                },
                                onDeleteClick = { deletedAddress ->
                                    deletedItem = deletedAddress
                                },
                                onItemSelected = {
                                    addressDetails!!.forEach {
                                        it.isSelected=false
                                       viewModel.addAddressData(it,context)
                                    }
                                    address.isSelected=!address.isSelected
                                    viewModel.addAddressData(address,context)
                                },
                                navController = navController,
                            )
                        }
                    }
                })
                Button(
                    onClick = {
                        scope.launch { sheetState.expand() }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff34495E)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.twenty))
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.ten)))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.ten)))
                    Text(
                        text = stringResource(id = R.string.addAddress),
                        color = Color.White
                    )
                }
            }
        }

    }
    LaunchedEffect(key1 = Unit, block = {
        scope.launch {
            viewModel.getData()
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressCard(
    viewModel: AddAddressViewModel = hiltViewModel(),
    addAddress: AddAddress,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onEditClick: (AddAddress) -> Unit,
    onDeleteClick: (AddAddress) -> Unit,
    navController: NavHostController,
    onItemSelected: () -> Unit={}
) {

    var profileData: HomeData? by remember {
        mutableStateOf(null)
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit, block = {
        scope.launch {
            homeViewModel.getData()
            homeViewModel.getDataInState.collect {
                profileData = it.isSuccess
            }

        }
    })
    if (profileData != null) {
        Card(
            border = BorderStroke(1.dp, Color.Black),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.ten))
                .clickable {
                    onItemSelected.invoke()
                    val totalPrice: Int? = navController.previousBackStackEntry?.savedStateHandle?.get("totalPrice")
                    val totalPriceWithQuantity: Int? =
                        navController.previousBackStackEntry?.savedStateHandle?.get("totalPriceWithQuantity")
                    navController.currentBackStackEntry?.savedStateHandle?.set("totalPrice", totalPrice)
                    navController.currentBackStackEntry?.savedStateHandle?.set("totalPriceWithQuantity", totalPriceWithQuantity)
                    navController.setData(Constants.currentStep, 2)
                    navController.setData("Add_address", addAddress)
                    viewModel.saveAddress(addAddress)
                    navController.navigate(NavRoute.PlaceOrder.route)
                }

        ) {
            val addressDetails = viewModel.address.value
            Column(modifier = Modifier.background(if (addressDetails?.id == addAddress.id) Color.LightGray else Color.White)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        profileData?.userName?.let {
                            Text(
                                text = it,
                                modifier = Modifier.padding(
                                    start = dimensionResource(id = R.dimen.twenty),
                                    top = dimensionResource(id = R.dimen.twenty),
                                    end = dimensionResource(id = R.dimen.ten),
                                    bottom = dimensionResource(id = R.dimen.ten)
                                ),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        AssistChip(onClick = { }, label = {
                            Text(addAddress.addressType)
                        }, leadingIcon = {
                            when (addAddress.addressType) {
                                Constants.home -> Icon(
                                    painter = painterResource(id = R.drawable.baseline_home_24),
                                    contentDescription = null
                                )

                                Constants.work -> Icon(
                                    painter = painterResource(id = R.drawable.baseline_work_24),
                                    contentDescription = null
                                )

                                Constants.other -> Icon(
                                    painter = painterResource(id = R.drawable.baseline_other_houses_24),
                                    contentDescription = null
                                )
                            }

                        }
                        )
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.twenty)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${addAddress.houseNumber},${addAddress.area},${addAddress.state},${addAddress.pincode}",
                        overflow = TextOverflow.Clip,
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.eight))
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            viewModel.deleteAddress(documentPath = addAddress.id, context)
                            onDeleteClick.invoke(addAddress)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = if (addressDetails?.id == addAddress.id) Color.LightGray else Color.White),
                        elevation = ButtonDefaults.elevation(0.dp),

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_24),
                            contentDescription = null,
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.ten)))
                        Text(
                            text = stringResource(id = R.string.delete),
                            color = Color.Red
                        )
                    }
                    Button(
                        onClick = {
                            onEditClick.invoke(addAddress)
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colors.background,
                            backgroundColor = if (addressDetails?.id == addAddress.id) Color.LightGray else Color.White
                        ),
                        elevation = ButtonDefaults.elevation(0.dp),

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_edit_24),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.ten)))
                        Text(
                            text = stringResource(id = R.string.edit),
                            color = MaterialTheme.colors.primary
                        )
                    }

                }
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun ChipSelectionList(items: List<String>, onItemSelected: (String) -> Unit = {}) {
    var selectedChips by remember { mutableStateOf(items[0]) }

    FlowRow {
        items.forEach { item ->
            AssistChip(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.eight)),
                onClick = {
                    selectedChips = item
                    onItemSelected(selectedChips)
                },
                leadingIcon = {
                    when (item) {
                        Constants.home -> Icon(
                            painter = painterResource(id = R.drawable.baseline_home_24),
                            contentDescription = null
                        )

                        Constants.work -> Icon(
                            painter = painterResource(id = R.drawable.baseline_work_24),
                            contentDescription = null
                        )

                        Constants.other -> Icon(
                            painter = painterResource(id = R.drawable.baseline_other_houses_24),
                            contentDescription = null
                        )
                    }


                },
                label = {
                    Text(text = item)
                },
                colors = AssistChipDefaults.assistChipColors(if (selectedChips == item) Color.LightGray else Color.White)
            )
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    addAddress: @Composable () -> Unit = {},
    payment: @Composable () -> Unit = {},
    orderSummary: @Composable () -> Unit = {},
    currentStepScreen: Int = 1,
    navController: NavHostController
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.sixteen)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val numberStep = 3

        var currentStep by rememberSaveable { mutableStateOf(currentStepScreen) }

        val titleList = arrayListOf(Constants.addAddress, Constants.orderSummary, Constants.payment)

        Stepper(
            modifier = Modifier.fillMaxWidth(),
            numberOfSteps = numberStep,
            currentStep = currentStep,
            stepDescriptionList = titleList,

            )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (currentStep) {
                1 -> addAddress.invoke()
                2 -> orderSummary.invoke()
                3 -> payment.invoke()
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

        /*    Button(
                onClick = { if (currentStep > 1) currentStep-- },
                enabled = currentStep > 1,
                modifier = Modifier.clip(RoundedCornerShape(15.dp))
            ) {
                Text(
                    text = stringResource(id = R.string.previous),
                    color = Color.White
                )
            }
            Button(
                onClick = { if (currentStep < numberStep) currentStep++ },
                enabled = currentStep < numberStep,
                modifier = Modifier.clip(RoundedCornerShape(15.dp))
            ) {
                Text(
                    text = stringResource(id = R.string.next),
                    color = Color.White
                )
            }*/

        }
    }
}