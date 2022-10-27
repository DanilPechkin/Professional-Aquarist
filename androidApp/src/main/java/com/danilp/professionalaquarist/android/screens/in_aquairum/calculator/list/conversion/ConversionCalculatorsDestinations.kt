package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.list.conversion

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Thermostat
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
        Icons.Default.Science,
        R.string.alkalinity_title
    ),
    Capacity(CapacityCalculatorDestination, Icons.Default.CropSquare, R.string.capacity_title),
    Metric(MetricCalculatorDestination, Icons.Default.Straighten, R.string.metric_title),
    Temperature(
        TemperatureCalculatorDestination,
        Icons.Default.Thermostat,
        R.string.temperature_title
    )
}
