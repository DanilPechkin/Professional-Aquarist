package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.list

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.domain.dweller.DwellerDataSource
import com.danilp.professionalaquarist.domain.dweller.SearchDwellers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DwellersListViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val dwellerDataSource: DwellerDataSource
) : ViewModel() {

    var state by mutableStateOf(DwellersListState())

    private var searchJob: Job? = null

    private val searchDwellers = SearchDwellers()

    init {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.in_aquarium_info_shared_preferences_key),
                Context.MODE_PRIVATE
            )
            state = state.copy(
                aquariumId = sharedPreferences.getLong(
                    context.getString(R.string.saved_aquarium_id_key),
                    0
                )
            )
            loadDwellers()
        }
    }

    fun onEvent(event: DwellersListEvent) {
        when (event) {
            is DwellersListEvent.Refresh -> {
                loadDwellers()
            }

            is DwellersListEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    loadDwellers()
                }
            }
        }
    }

    private fun loadDwellers(
        aquariumId: Long = state.aquariumId,
        name: String = state.searchQuery.lowercase()
    ) {
        viewModelScope.launch {
            state = state.copy(
                dwellers = searchDwellers.execute(
                    dwellerDataSource.getAllDwellersByAquarium(aquariumId),
                    name
                )
            )
        }
    }
}