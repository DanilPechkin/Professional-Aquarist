package com.danilp.professionalaquarist.android.screens.aquarium.list

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danilp.professionalaquarist.android.screens.GridItem
import com.danilp.professionalaquarist.domain.aquarium.Aquarium

@Composable
fun AquariumListItem(
    aquarium: Aquarium,
    message: String,
    modifier: Modifier = Modifier
) {
    GridItem(
        name = aquarium.name,
        // TODO: make message
        message = message,
        imageUri = aquarium.imageUrl,
        cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    )
}
