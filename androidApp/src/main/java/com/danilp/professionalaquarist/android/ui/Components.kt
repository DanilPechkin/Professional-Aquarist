package com.danilp.professionalaquarist.android.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.android.screens.top_bar_menu.settings.Language
import com.danilp.professionalaquarist.domain.aquarium.ComfortTags
import com.danilp.professionalaquarist.domain.dweller.tags.DwellerTags
import com.danilp.professionalaquarist.domain.plant.tags.PlantTags
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.MetricMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure
import com.danilp.professionalaquarist.domain.use_case.validation.ValidationError
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    isSearchFieldVisible: Boolean,
    hideSearchField: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = isSearchFieldVisible) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { hideSearchField() }),
            placeholder = {
                Text(text = stringResource(R.string.search_placeholder))
            },
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
                .focusRequester(focusRequester)
                .sizeIn(maxWidth = 192.dp)
        )

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AquariumTopBar(
    title: String,
    switchMenuVisibility: () -> Unit,
    isMenuExpanded: Boolean,
    hideMenu: () -> Unit,
    navigateBack: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToAccount: () -> Unit,
    navigationIcon: ImageVector = Icons.Rounded.ArrowBack
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.padding(horizontal = 4.dp),
                maxLines = 1
            )
        },
        navigationIcon = {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = stringResource(R.string.back_arrow_button)
                )
            }
        },
        actions = {
            Box {
                IconButton(
                    onClick = switchMenuVisibility
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(
                            R.string.expand_upbar_menu_content_description
                        )
                    )
                }
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = hideMenu,
                    offset = DpOffset((10).dp, 0.dp)
                ) {
                    DropdownMenuItem(
                        onClick = navigateToAccount,
                        text = {
                            Text(
                                text = stringResource(
                                    R.string.account_title
                                )
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.AccountCircle,
                                contentDescription = stringResource(
                                    R.string.account_title
                                )
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = navigateToSettings,
                        text = {
                            Text(
                                text = stringResource(
                                    R.string.settings_title
                                )
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = stringResource(
                                    R.string.settings_title
                                )
                            )
                        }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AquariumTopBarWithSearch(
    title: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isSearchFieldVisible: Boolean,
    switchSearchFieldVisibility: () -> Unit,
    hideSearchField: () -> Unit,
    searchFieldFocusRequester: FocusRequester,
    switchMenuVisibility: () -> Unit,
    isMenuExpanded: Boolean,
    hideMenu: () -> Unit,
    navigateBack: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToAccount: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.padding(horizontal = 4.dp),
                maxLines = 1
            )
        },
        navigationIcon = {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.back_arrow_button)
                )
            }
        },
        actions = {
            SearchField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                isSearchFieldVisible = isSearchFieldVisible,
                hideSearchField = hideSearchField,
                focusRequester = searchFieldFocusRequester
            )

            IconButton(
                onClick = switchSearchFieldVisibility
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(
                        R.string.search_icon
                    )
                )
            }

            Box {
                IconButton(
                    onClick = switchMenuVisibility
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(
                            R.string.expand_upbar_menu_content_description
                        )
                    )
                }
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = hideMenu,
                    offset = DpOffset((10).dp, 0.dp)
                ) {
                    DropdownMenuItem(
                        onClick = navigateToAccount,
                        text = {
                            Text(
                                text = stringResource(
                                    R.string.account_title
                                )
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.AccountCircle,
                                contentDescription = stringResource(
                                    R.string.account_title
                                )
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = navigateToSettings,
                        text = {
                            Text(
                                text = stringResource(
                                    R.string.settings_title
                                )
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = stringResource(
                                    R.string.settings_title
                                )
                            )
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun GridItem(
    label: String?,
    message: String,
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        GlideImage(
            imageModel = imageUrl ?: R.drawable.aquairum_pic,
            contentDescription = label ?: "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .heightIn(max = 110.dp)
        )
        Column(
            modifier = Modifier.padding(top = 6.dp, start = 10.dp, bottom = 10.dp, end = 10.dp)
        ) {
            Text(text = label ?: "", style = MaterialTheme.typography.titleMedium, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when (message) {
                    ComfortTags.VERY_SATISFIED.code -> stringResource(R.string.very_satisfied)
                    ComfortTags.SATISFIED.code -> stringResource(R.string.satisfied)
                    ComfortTags.NEUTRAL.code -> stringResource(R.string.neutral_label)
                    ComfortTags.DISSATISFIED.code -> stringResource(R.string.dissatisfied)
                    ComfortTags.VERY_DISSATISFIED.code ->
                        stringResource(R.string.very_dissatisfied)

                    else -> message
                },
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun AquaristListItem(
    onClick: () -> Unit,
    icon: ImageVector,
    name: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                modifier = Modifier
                    .padding(8.dp)
                    .size(48.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1.4f))
        }
    }
}

@Composable
fun ImagePicker(
    imageUri: String?,
    onSelection: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        onSelection(it?.toString() ?: "")
    }

    GlideImage(
        imageModel = imageUri ?: R.drawable.aquairum_pic,
        contentDescription = stringResource(R.string.imagepicker_content_descr),
        contentScale = ContentScale.Crop,
        // TODO: placeholder,
        modifier = modifier
            .clickable {
                pickMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoFieldWithErrorAndIcon(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    errorCode: Int? = null,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    alkalinityMeasureCode: Int? = null,
    temperatureMeasureCode: Int? = null,
    capacityMeasureCode: Int? = null,
    metricMeasureCode: Int? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    maxLines = 1
                )
            },
            modifier = textFieldModifier,
            isError = errorCode != null,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            singleLine = singleLine,
            trailingIcon = {
                if (alkalinityMeasureCode != null) {
                    Text(
                        text = when (alkalinityMeasureCode) {
                            AlkalinityMeasure.DKH.code ->
                                stringResource(R.string.alkalinity_measure_dh_short)

                            AlkalinityMeasure.MEQL.code ->
                                stringResource(R.string.alkalinity_measure_meql_short)

                            AlkalinityMeasure.MGL.code ->
                                stringResource(R.string.alkalinity_measure_mgl_short)

                            AlkalinityMeasure.PPM.code ->
                                stringResource(R.string.alkalinity_measure_ppm_short)

                            else ->
                                ""
                        }
                    )
                }
                if (temperatureMeasureCode != null) {
                    Text(
                        text = when (temperatureMeasureCode) {
                            TemperatureMeasure.Celsius.code ->
                                stringResource(R.string.temperature_measure_celsius_short)

                            TemperatureMeasure.Fahrenheit.code ->
                                stringResource(R.string.temperature_measure_fahrenheit_short)

                            TemperatureMeasure.Kelvin.code ->
                                stringResource(R.string.temperature_measure_kelvin_short)

                            else ->
                                ""
                        }
                    )
                }
                if (capacityMeasureCode != null) {
                    Text(
                        text = when (capacityMeasureCode) {
                            CapacityMeasure.Liters.code ->
                                stringResource(R.string.capacity_measure_liters)

                            CapacityMeasure.Gallons.code ->
                                stringResource(R.string.capacity_measure_gallons)

                            CapacityMeasure.CubicFeet.code ->
                                stringResource(R.string.capacity_measure_cubic_feet)

                            CapacityMeasure.USCups.code ->
                                stringResource(R.string.capacity_measure_us_cups)

                            CapacityMeasure.Teaspoons.code ->
                                stringResource(R.string.capacity_measure_teaspoons)

                            CapacityMeasure.Tablespoons.code ->
                                stringResource(R.string.capacity_measure_tablespoons)

                            CapacityMeasure.Milliliters.code ->
                                stringResource(R.string.capacity_measure_milliliters)

                            CapacityMeasure.MetricCups.code ->
                                stringResource(R.string.capacity_measure_metric_cups)

                            CapacityMeasure.CubicMeters.code ->
                                stringResource(R.string.capacity_measure_cubic_meters)

                            CapacityMeasure.CubicInches.code ->
                                stringResource(R.string.capacity_measure_cubic_inches)

                            CapacityMeasure.CubicCentimeters.code ->
                                stringResource(R.string.capacity_measure_cubic_centimeters)

                            else ->
                                ""
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                if (metricMeasureCode != null) {
                    Text(
                        text = when (metricMeasureCode) {
                            MetricMeasure.Millimeters.code ->
                                stringResource(R.string.metric_measure_millimeters_short)

                            MetricMeasure.Centimeters.code ->
                                stringResource(R.string.metric_measure_centimeters_short)

                            MetricMeasure.Meters.code ->
                                stringResource(R.string.metric_measure_meters_short)

                            MetricMeasure.Inches.code ->
                                stringResource(R.string.metric_measure_inches_short)

                            MetricMeasure.Feet.code ->
                                stringResource(R.string.metric_measure_feet_short)

                            else -> ""
                        }
                    )
                }
            }
        )

        if (errorCode != null) {
            Text(
                text = when (errorCode) {
                    ValidationError.BLANK_FIELD_ERROR.code -> {
                        stringResource(R.string.this_field_cant_be_blank_error)
                    }

                    ValidationError.DECIMAL_ERROR.code -> {
                        stringResource(R.string.should_be_decimal_error)
                    }

                    ValidationError.INTEGER_ERROR.code -> {
                        stringResource(R.string.should_be_integer_error)
                    }

                    ValidationError.NEGATIVE_VALUE_ERROR.code -> {
                        stringResource(R.string.this_value_cant_be_negative_error)
                    }

                    else -> {
                        stringResource(R.string.unknown_error)
                    }
                },
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoFieldWithError(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    errorCode: Int? = null,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    maxLines = 1
                )
            },
            modifier = textFieldModifier,
            isError = errorCode != null,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            singleLine = singleLine
        )

        if (errorCode != null) {
            Text(
                text = when (errorCode) {
                    ValidationError.BLANK_FIELD_ERROR.code -> {
                        stringResource(R.string.this_field_cant_be_blank_error)
                    }

                    ValidationError.DECIMAL_ERROR.code -> {
                        stringResource(R.string.should_be_decimal_error)
                    }

                    ValidationError.INTEGER_ERROR.code -> {
                        stringResource(R.string.should_be_integer_error)
                    }

                    ValidationError.NEGATIVE_VALUE_ERROR.code -> {
                        stringResource(R.string.this_value_cant_be_negative_error)
                    }

                    else -> {
                        stringResource(R.string.unknown_error)
                    }
                },
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun FromToInfoFields(
    label: String,
    valueFrom: String,
    valueTo: String,
    onValueFromChange: (String) -> Unit,
    onValueToChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Decimal,
        imeAction = ImeAction.Next
    ),
    keyboardActionsFrom: KeyboardActions = KeyboardActions(),
    keyboardActionsTo: KeyboardActions = KeyboardActions(),
    errorCodeFrom: Int? = null,
    errorCodeTo: Int? = null,
    alkalinityMeasureCode: Int? = null,
    temperatureMeasureCode: Int? = null,
    capacityMeasureCode: Int? = null,
    metricMeasureCode: Int? = null
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            maxLines = 1,
            modifier = Modifier
                .padding(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoFieldWithErrorAndIcon(
                value = valueFrom,
                onValueChange = onValueFromChange,
                label = stringResource(R.string.min_label),
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActionsFrom,
                errorCode = errorCodeFrom,
                maxLines = 1,
                singleLine = true,
                alkalinityMeasureCode = alkalinityMeasureCode,
                temperatureMeasureCode = temperatureMeasureCode,
                capacityMeasureCode = capacityMeasureCode,
                metricMeasureCode = metricMeasureCode
            )
            InfoFieldWithErrorAndIcon(
                value = valueTo,
                onValueChange = onValueToChange,
                label = stringResource(R.string.max_label),
                modifier = Modifier.weight(1f),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActionsTo,
                errorCode = errorCodeTo,
                maxLines = 1,
                singleLine = true,
                alkalinityMeasureCode = alkalinityMeasureCode,
                temperatureMeasureCode = temperatureMeasureCode,
                capacityMeasureCode = capacityMeasureCode,
                metricMeasureCode = metricMeasureCode
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedDropDownMenuField(
    label: String,
    items: List<String>,
    selectedItem: String,
    changeSelectedItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Rounded.KeyboardArrowUp
    else
        Icons.Rounded.KeyboardArrowDown

    Column(modifier = modifier) {

        OutlinedTextField(
            value = selectedItem,
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            maxLines = 1,
            singleLine = true,
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(R.string.expand_button_content_descr)
                    )
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(
                    with(LocalDensity.current) {
                        textFieldSize.width.toDp()
                    }
                )
        ) {
            items.forEach { label ->
                DropdownMenuItem(
                    text = { Text(text = label) },
                    onClick = {
                        changeSelectedItem(label)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownLanguageSelector(
    label: String,
    items: List<Language>,
    selectedItem: Language,
    changeSelectedItem: (Language) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Rounded.KeyboardArrowUp
    else
        Icons.Rounded.KeyboardArrowDown

    Column(modifier = modifier) {

        OutlinedTextField(
            value = selectedItem.language,
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            maxLines = 1,
            singleLine = true,
            readOnly = true,
            label = { Text(text = label) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = selectedItem.flag),
                    contentDescription = selectedItem.language,
                    modifier = Modifier.padding(start = 8.dp)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(R.string.expand_button_content_descr)
                    )
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(
                    with(LocalDensity.current) {
                        textFieldSize.width.toDp()
                    }
                )
        ) {
            items.forEach { language ->
                DropdownMenuItem(
                    text = { Text(text = language.language) },
                    leadingIcon = {
                        Image(
                            painter = painterResource(language.flag),
                            contentDescription = language.language
                        )
                    },
                    onClick = {
                        changeSelectedItem(language)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun GridTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun SelectChip(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    labelCode: String? = null,
    isError: Boolean = false
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label ?: when (labelCode) {
                    PlantTags.BROADLEAF_PLANT.code ->
                        stringResource(R.string.broadleaf_title)

                    PlantTags.LONG_STEMMED_PLANT.code ->
                        stringResource(R.string.long_stemmed_title)

                    PlantTags.FLOATING_PLANT.code ->
                        stringResource(R.string.floating_plant_title)

                    PlantTags.MOSS.code ->
                        stringResource(R.string.moss_title)

                    PlantTags.FERN.code ->
                        stringResource(R.string.fern_title)

                    PlantTags.BRIGHT_LIGHT.code ->
                        stringResource(R.string.needs_bright_light)

                    PlantTags.LOW_LIGHT.code ->
                        stringResource(R.string.needs_low_light)

                    DwellerTags.CRAB.code ->
                        stringResource(R.string.crab_title)

                    DwellerTags.SNAIL.code ->
                        stringResource(R.string.snail_title)

                    DwellerTags.SHRIMP.code ->
                        stringResource(R.string.shrimp_title)

                    DwellerTags.FISH.code ->
                        stringResource(R.string.fish_title)

                    DwellerTags.BIVALVE.code ->
                        stringResource(R.string.bivalve_title)

                    DwellerTags.CRAYFISH.code ->
                        stringResource(R.string.crayfish_title)

                    DwellerTags.FAST_CURRENT.code ->
                        stringResource(R.string.needs_fast_current_label)

                    DwellerTags.SLOW_CURRENT.code ->
                        stringResource(R.string.needs_slow_current_label)

                    DwellerTags.MEDIUM_CURRENT.code ->
                        stringResource(R.string.needs_medium_current_label)

                    DwellerTags.BRIGHT_LIGHT.code ->
                        stringResource(R.string.needs_bright_light_label)

                    DwellerTags.LOW_LIGHT.code ->
                        stringResource(R.string.needs_low_light_label)

                    DwellerTags.PLANT_LOVER.code ->
                        stringResource(R.string.needs_lot_of_plants_label)

                    DwellerTags.NEEDS_SHELTER.code ->
                        stringResource(R.string.needs_shelter_label)

                    DwellerTags.BROADLEAF_PLANT.code ->
                        stringResource(R.string.needs_broadleaf_plant_label)

                    DwellerTags.LONG_STEMMED_PLANT.code ->
                        stringResource(R.string.needs_long_stemmed_label)

                    DwellerTags.FLOATING_PLANT.code ->
                        stringResource(R.string.needs_floating_plant_label)

                    DwellerTags.MOSS.code ->
                        stringResource(R.string.needs_moss_label)

                    DwellerTags.HERBIVOROUS.code ->
                        stringResource(R.string.herbivorous_label)

                    DwellerTags.CARNIVOROUS.code ->
                        stringResource(R.string.carnivorous_label)

                    DwellerTags.OMNIVOROUS.code ->
                        stringResource(R.string.omnivorous_label)

                    DwellerTags.PREDATOR.code ->
                        stringResource(R.string.predator_label)

                    DwellerTags.PEACEFUL.code ->
                        stringResource(R.string.peaceful_label)

                    DwellerTags.TERRITORIAL.code ->
                        stringResource(R.string.territorial_label)

                    DwellerTags.NEEDS_SMOOTH_SURFACES.code ->
                        stringResource(R.string.veil_tailed_label)

                    DwellerTags.LARGE.code ->
                        stringResource(R.string.large_label)

                    DwellerTags.BIG.code ->
                        stringResource(R.string.big_label)

                    DwellerTags.MEDIUM.code ->
                        stringResource(R.string.medium_label)

                    DwellerTags.SMALL.code ->
                        stringResource(R.string.small_label)

                    DwellerTags.MONOGAMOUS.code ->
                        stringResource(R.string.monogamous_label)

                    DwellerTags.POLYGAMOUS.code ->
                        stringResource(R.string.polygamous_label)

                    DwellerTags.LIVEBEARER.code ->
                        stringResource(R.string.livebearer_label)

                    DwellerTags.OVIPAROUS.code ->
                        stringResource(R.string.oviparous_label)

                    DwellerTags.SHOAL.code ->
                        stringResource(R.string.shoal_label)

                    DwellerTags.CLEANER.code ->
                        stringResource(R.string.cleaner_label)

                    DwellerTags.PLANT_EATER.code ->
                        stringResource(R.string.plant_eater_label)

                    DwellerTags.NEEDS_DRIFTWOOD.code ->
                        stringResource(R.string.needs_driftwood_label)

                    else -> labelCode ?: ""
                }
            )
        },
        leadingIcon = {
            AnimatedVisibility(
                visible = selected,
                enter = expandIn() + scaleIn() + fadeIn(),
                exit = shrinkOut() + scaleOut() + fadeOut()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Done,
                    contentDescription = stringResource(androidx.compose.ui.R.string.selected),
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        },
        colors = if (isError) {
            FilterChipDefaults.filterChipColors(
                labelColor = MaterialTheme.colorScheme.error
            )
        } else {
            FilterChipDefaults.filterChipColors()
        },
        border = if (isError) {
            FilterChipDefaults.filterChipBorder(
                borderColor = MaterialTheme.colorScheme.error
            )
        } else {
            FilterChipDefaults.filterChipBorder()
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AquariumSelectChip(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    labelCode: String? = null,
    isError: Boolean = false
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label ?: when (labelCode) {
                    PlantTags.BROADLEAF_PLANT.code ->
                        stringResource(R.string.broadleaf_title)

                    PlantTags.LONG_STEMMED_PLANT.code ->
                        stringResource(R.string.long_stemmed_title)

                    PlantTags.FLOATING_PLANT.code ->
                        stringResource(R.string.floating_plant_title)

                    PlantTags.MOSS.code ->
                        stringResource(R.string.moss_title)

                    PlantTags.FERN.code ->
                        stringResource(R.string.fern_title)

                    PlantTags.BRIGHT_LIGHT.code ->
                        stringResource(R.string.bright_light_label)

                    PlantTags.LOW_LIGHT.code ->
                        stringResource(R.string.low_light_label)

                    DwellerTags.FAST_CURRENT.code ->
                        stringResource(R.string.fast_current_label)

                    DwellerTags.SLOW_CURRENT.code ->
                        stringResource(R.string.slow_current_label)

                    DwellerTags.MEDIUM_CURRENT.code ->
                        stringResource(R.string.medium_current_label)

                    DwellerTags.BRIGHT_LIGHT.code ->
                        stringResource(R.string.bright_light_label)

                    DwellerTags.LOW_LIGHT.code ->
                        stringResource(R.string.low_light_label)

                    DwellerTags.PLANT_LOVER.code ->
                        stringResource(R.string.lot_of_plants_label)

                    DwellerTags.NEEDS_SHELTER.code ->
                        stringResource(R.string.shelter_available_label)

                    DwellerTags.NEEDS_SMOOTH_SURFACES.code ->
                        stringResource(R.string.favorable_for_veil_tailed_label)

                    DwellerTags.NEEDS_DRIFTWOOD.code ->
                        stringResource(R.string.driftwood_available_label)

                    else -> labelCode ?: ""
                }
            )
        },
        leadingIcon = {
            AnimatedVisibility(
                visible = selected,
                enter = expandIn() + scaleIn() + fadeIn(),
                exit = shrinkOut() + scaleOut() + fadeOut()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Done,
                    contentDescription = stringResource(androidx.compose.ui.R.string.selected),
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        },
        colors = if (isError) {
            FilterChipDefaults.filterChipColors(
                labelColor = MaterialTheme.colorScheme.error
            )
        } else {
            FilterChipDefaults.filterChipColors()
        },
        border = if (isError) {
            FilterChipDefaults.filterChipBorder(
                borderColor = MaterialTheme.colorScheme.error
            )
        } else {
            FilterChipDefaults.filterChipBorder()
        },
        modifier = modifier
    )
}
