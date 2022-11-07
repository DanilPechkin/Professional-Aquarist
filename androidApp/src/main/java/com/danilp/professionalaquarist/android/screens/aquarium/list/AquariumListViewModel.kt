package com.danilp.professionalaquarist.android.screens.aquarium.list

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.screens.top_bar_menu.settings.SharedPrefs
import com.danilp.professionalaquarist.domain.aquarium.AquariumDataSource
import com.danilp.professionalaquarist.domain.aquarium.SearchAquariums
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class AquariumListViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val aquariumDataSource: AquariumDataSource,
    private val searchAquariums: SearchAquariums
) : ViewModel() {

    var state by mutableStateOf(AquariumListState())

    private var sharedPreferences: SharedPreferences? = null

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            sharedPreferences = context.getSharedPreferences(
                SharedPrefs.InAquariumInfo.key,
                Context.MODE_PRIVATE
            )
        }
    }

    fun onEvent(event: AquariumListEvent) {
        when (event) {
            is AquariumListEvent.Refresh -> {
                loadAquariums()
            }

            is AquariumListEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    loadAquariums()
                }
            }

            is AquariumListEvent.OnAquariumClicked -> {
                viewModelScope.launch {
                    saveAquariumId(event.aquariumId)
                }
            }
        }
    }

    private fun loadAquariums(
        name: String = state.searchQuery.lowercase()
    ) {
        viewModelScope.launch {
            state = state.copy(
                aquariums = searchAquariums.execute(
                    aquariumDataSource.getAllAquariums(),
                    name
                )
            )
        }
    }

    private fun saveAquariumId(id: Long) {
        with(sharedPreferences?.edit()) {
            this?.putLong(SharedPrefs.CurrentAquarium.key, id) ?: return
            commit()
        }
    }
}
