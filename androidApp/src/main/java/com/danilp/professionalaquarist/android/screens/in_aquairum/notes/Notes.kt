package com.danilp.professionalaquarist.android.screens.in_aquairum.notes

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.android.navigation.nav_graphs.InAquariumNavGraph
import com.danilp.professionalaquarist.android.screens.destinations.AquariumListDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@InAquariumNavGraph
@Destination
@Composable
fun Notes(
    navigator: DestinationsNavigator
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.notes_title))
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
        Text(text = "Notes", modifier = Modifier.padding(paddingValues))
    }
}
