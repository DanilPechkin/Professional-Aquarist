package com.danilp.professionalaquarist.android.screens.in_aquairum.plant.list

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.screens.top_bar_menu.settings.SharedPrefs
import com.danilp.professionalaquarist.domain.plant.PlantDataSource
import com.danilp.professionalaquarist.domain.plant.SearchPlants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class PlantsListViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val plantDataSource: PlantDataSource
) : ViewModel() {

    var state by mutableStateOf(PlantsListState())

    private var searchJob: Job? = null

    private val searchPlants = SearchPlants()

    init {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences(
                SharedPrefs.InAquariumInfo.key,
                Context.MODE_PRIVATE
            )
            state = state.copy(
                aquariumId = sharedPreferences.getLong(
                    SharedPrefs.CurrentAquarium.key,
                    0
                )
            )
            loadPlants()
        }
    }

    fun onEvent(event: PlantsListEvent) {
        when (event) {
            is PlantsListEvent.Refresh -> {
                loadPlants()
            }

            is PlantsListEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    loadPlants()
                }
            }
        }
    }

    private fun loadPlants(
        aquariumId: Long = state.aquariumId,
        name: String = state.searchQuery.lowercase()
    ) {
        viewModelScope.launch {
            state = state.copy(
                plants = searchPlants.execute(
                    plantDataSource.getAllPlantsByAquarium(aquariumId),
                    name
                )
            )
        }
    }
}
