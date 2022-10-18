package com.danilp.professionalaquarist.android.screens.in_aquairum.in_aquarium_bottom_bar

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.danilp.professionalaquarist.android.screens.destinations.Destination
import com.ramcosta.composedestinations.navigation.navigate

@Composable
fun AquariumBottomBar(
    navController: NavController,
    currentDestination: Destination,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier
    ) {
        AquariumBottomBarDestination.values().forEach { destination ->
            NavigationBarItem(
                selected = destination.direction == currentDestination,
                onClick = {
                    navController.navigate(destination.direction) { launchSingleTop = true }
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.label)
                    )
                }
            )
        }
    }
}
