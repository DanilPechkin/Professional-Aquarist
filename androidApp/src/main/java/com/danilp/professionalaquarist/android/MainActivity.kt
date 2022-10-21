package com.danilp.professionalaquarist.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.danilp.professionalaquarist.android.screens.aquarium.aquariums_screen.AquariumsScreen
import com.danilp.professionalaquarist.android.ui.theme.ProfessionalAquaristTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfessionalAquaristTheme {
                AquariumsScreen()
            }
        }
    }
}
