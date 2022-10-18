package com.danilp.professionalaquarist.android.screens.in_aquairum.in_aquarium_bottom_bar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.SetMeal
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.Water
import androidx.compose.ui.graphics.vector.ImageVector
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.android.screens.destinations.CalculatorsDestination
import com.danilp.professionalaquarist.android.screens.destinations.DwellersListDestination
import com.danilp.professionalaquarist.android.screens.destinations.MainAquariumScreenDestination
import com.danilp.professionalaquarist.android.screens.destinations.NotesDestination
import com.danilp.professionalaquarist.android.screens.destinations.PlantsListDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

//TODO: add nice icons

enum class AquariumBottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Notes(NotesDestination, Icons.Rounded.FormatListBulleted, R.string.notes_icon),
    Calculators(CalculatorsDestination, Icons.Rounded.Calculate, R.string.calculators_icon),
    Main(MainAquariumScreenDestination, Icons.Rounded.Water, R.string.main_aquarium_icon),
    Dwellers(DwellersListDestination, Icons.Rounded.SetMeal, R.string.dwellers_icon),
    Plants(PlantsListDestination, Icons.Rounded.Spa, R.string.plants_icon)
}