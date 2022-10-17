package com.danilp.professionalaquarist.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.danilp.professionalaquarist.android.screens.aquarium.aquariums_screen.AquariumsScreen
import com.danilp.professionalaquarist.android.ui.theme.ProfessinalAquaristTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfessinalAquaristTheme {
                AquariumsScreen()
            }
        }
    }
}