package com.danilp.professionalaquarist.android.screens.in_aquairum.plant.list

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danilp.professionalaquarist.android.screens.GridItem
import com.danilp.professionalaquarist.domain.plant.Plant

@Composable
fun PlantsListItem(
    plant: Plant,
    modifier: Modifier = Modifier
) {
    GridItem(
        name = plant.name,
        // TODO: make message
        message = "Здоровый",
        imageUri = plant.imageUrl,
        cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    )
}
