package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.list

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danilp.professionalaquarist.android.screens.GridItem
import com.danilp.professionalaquarist.domain.dweller.Dweller

@Composable
fun DwellersListItem(
    dweller: Dweller,
    modifier: Modifier = Modifier
) {
    GridItem(
        name = dweller.name,
        message = "Здоровый",
        imageUri = dweller.imageUrl,
        cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    )
}
