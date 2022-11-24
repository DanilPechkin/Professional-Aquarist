package com.danilp.professionalaquarist.android.screens.aquarium.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.android.screens.destinations.AquariumListDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
import com.danilp.professionalaquarist.android.ui.AquariumTopBar
import com.danilp.professionalaquarist.android.ui.ImagePicker
import com.danilp.professionalaquarist.android.ui.InfoFieldWithErrorAndIcon
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph
@Destination
@Composable
fun AquariumEdit(
    navigator: DestinationsNavigator,
    viewModel: AquariumCreateViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var isTopMenuExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = LocalContext.current) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is AquariumCreateViewModel.ValidationEvent.Success -> {
                    navigator.navigate(AquariumListDestination)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AquariumTopBar(
                stringResource(R.string.create_aquarium_title),
                switchMenuVisibility = { isTopMenuExpanded = !isTopMenuExpanded },
                isMenuExpanded = isTopMenuExpanded,
                hideMenu = { isTopMenuExpanded = false },
                navigateBack = { navigator.navigateUp() },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination()) },
                navigateToAccount = { },
                Icons.Rounded.Close
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(R.string.save_button)) },
                icon = { Icon(Icons.Rounded.Save, stringResource(R.string.save_button)) },
                onClick = { viewModel.onEvent(AquariumCreateEvent.InsertButtonPressed) },
                expanded = scrollState.value == 0
            )
        }
    ) { paddingValues ->

        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            )
        }

        Column(
            Modifier
                .padding(paddingValues)
                .padding(top = 0.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState)
        ) {

            val focusManager = LocalFocusManager.current

            ImagePicker(
                imageUri = state.aquarium.imageUrl,
                onSelection = { viewModel.onEvent(AquariumCreateEvent.ImagePicked(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 256.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithErrorAndIcon(
                value = state.name,
                onValueChange = { viewModel.onEvent(AquariumCreateEvent.NameChanged(it)) },
                label = stringResource(R.string.name_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorCode = state.nameErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithErrorAndIcon(
                value = state.liters,
                onValueChange = { viewModel.onEvent(AquariumCreateEvent.LitersChanged(it)) },
                label = stringResource(R.string.capacity_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorCode = state.litersErrorCode,
                maxLines = 1,
                singleLine = true,
                capacityMeasureCode = state.capacityMeasureCode,
                modifier = Modifier.fillMaxWidth(),
                textFieldModifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onEvent(AquariumCreateEvent.DescriptionChanged(it)) },
                label = {
                    Text(text = stringResource(R.string.description_label))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithErrorAndIcon(
                value = state.currentIllumination,
                onValueChange = {
                    viewModel.onEvent(AquariumCreateEvent.CurrentIlluminationChanged(it))
                },
                label = stringResource(R.string.illumination_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                errorCode = state.currentIlluminationErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth()
            )
        }
    }
}
