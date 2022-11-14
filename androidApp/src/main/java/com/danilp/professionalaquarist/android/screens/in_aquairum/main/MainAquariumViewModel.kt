package com.danilp.professionalaquarist.android.screens.in_aquairum.main

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.screens.top_bar_menu.settings.SharedPrefs
import com.danilp.professionalaquarist.domain.aquarium.Aquarium
import com.danilp.professionalaquarist.domain.aquarium.AquariumDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainAquariumViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val aquariumDataSource: AquariumDataSource
) : ViewModel() {

    var state by mutableStateOf(MainAquariumState())

    init {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences(
                SharedPrefs.InAquariumInfo.key,
                Context.MODE_PRIVATE
            )

            val aquariumId = sharedPreferences.getLong(
                SharedPrefs.CurrentAquarium.key,
                0
            )

            state = state.copy(
                aquarium = aquariumDataSource.getAquariumById(aquariumId) ?: Aquarium.createEmpty()
            )
        }
    }
}
