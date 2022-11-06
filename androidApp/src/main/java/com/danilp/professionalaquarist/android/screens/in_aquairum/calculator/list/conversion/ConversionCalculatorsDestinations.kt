package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.list.conversion

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CropSquare
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.Straighten
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.ui.graphics.vector.ImageVector
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.android.screens.destinations.AlkalinityCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.CapacityCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.MetricCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.TemperatureCalculatorDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class ConversionCalculatorsDestinations(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val title: Int
) {
    Alkalinity(
        AlkalinityCalculatorDestination,
        Icons.Rounded.Science,
        R.string.alkalinity_title
    ),
    Capacity(CapacityCalculatorDestination, Icons.Rounded.CropSquare, R.string.capacity_title),
    Metric(MetricCalculatorDestination, Icons.Rounded.Straighten, R.string.metric_title),
    Temperature(
        TemperatureCalculatorDestination,
        Icons.Rounded.Thermostat,
        R.string.temperature_title
    )
}
