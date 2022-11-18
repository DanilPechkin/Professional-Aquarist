package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators_main_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.android.screens.destinations.CapacityCalculatorsListDestination
import com.danilp.professionalaquarist.android.screens.destinations.ConversionCalculatorsListDestination
import com.danilp.professionalaquarist.android.screens.destinations.CylinderCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.MetricCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
import com.danilp.professionalaquarist.android.ui.AquariumTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph
@Destination
@Composable
fun Calculators(
    navigator: DestinationsNavigator
) {
    var isTopMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AquariumTopBar(
                title = stringResource(R.string.calculators_title),
                switchMenuVisibility = { isTopMenuExpanded = !isTopMenuExpanded },
                isMenuExpanded = isTopMenuExpanded,
                hideMenu = { isTopMenuExpanded = false },
                navigateBack = { navigator.navigateUp() },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination()) },
                navigateToAccount = { }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(32.dp)
            ) {
                Row {
                    CalculatorsCard(
                        icon = Icons.Rounded.Calculate,
                        label = stringResource(R.string.conversion_title),
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .clickable {
                                navigator.navigate(ConversionCalculatorsListDestination)
                            }
                    )
                    CalculatorsCard(
                        icon = Icons.Rounded.Calculate,
                        label = "capacity",
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .clickable {
                                navigator.navigate(CylinderCalculatorDestination)
                            }
                    )
                }
                Row {
                    CalculatorsCard(
                        icon = Icons.Rounded.Calculate,
                        label = stringResource(R.string.capacity_title),
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .clickable {
                                navigator.navigate(CapacityCalculatorsListDestination)
                            }
                    )
                    CalculatorsCard(
                        icon = Icons.Rounded.Calculate,
                        label = "illumination",
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .clickable {
                                navigator.navigate(MetricCalculatorDestination)
                            }
                    )
                }
            }
        }
    }
}
