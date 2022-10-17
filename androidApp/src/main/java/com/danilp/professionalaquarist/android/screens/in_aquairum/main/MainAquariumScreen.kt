package com.danilp.professionalaquarist.android.screens.in_aquairum.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danilp.professionalaquarist.android.R
import androidx.compose.ui.res.stringResource
import com.danilp.professionalaquarist.android.navigation.nav_graphs.InAquariumNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@InAquariumNavGraph(start = true)
@Destination
@Composable
fun MainAquariumScreen(
    navigator: DestinationsNavigator
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = stringResource(R.string.aquarium_title))
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigate(AquariumListDestination()) }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.back_arrow_button)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Text(text = "Main", modifier = Modifier.padding(paddingValues))
    }
}