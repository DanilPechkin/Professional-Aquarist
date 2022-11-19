package com.danilp.professionalaquarist.android.screens.in_aquairum.plant.info

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.screens.top_bar_menu.settings.SharedPrefs
import com.danilp.professionalaquarist.domain.plant.Plant
import com.danilp.professionalaquarist.domain.plant.PlantDataSource
import com.danilp.professionalaquarist.domain.plant.use_case.ConvertPlantMeasures
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantInfoViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val convertPlantMeasures: ConvertPlantMeasures,
    private val plantDataSource: PlantDataSource,
) : ViewModel() {

    var state by mutableStateOf(PlantInfoState())

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<Long>("plantId") ?: return@launch

            state = state.copy(
                plant = plantDataSource.getPlantById(id) ?: Plant.createEmpty()
            )

            val sharedPreferences = context.getSharedPreferences(
                SharedPrefs.InAquariumInfo.key,
                Context.MODE_PRIVATE
            )

            state = state.copy(
                plant = state.plant.copy(
                    aquariumId = sharedPreferences.getLong(
                        SharedPrefs.CurrentAquarium.key,
                        0
                    )
                ),
                temperatureMeasureCode = sharedPreferences.getInt(
                    SharedPrefs.TemperatureMeasure.key,
                    TemperatureMeasure.Celsius.code
                ),
                alkalinityMeasureCode = sharedPreferences.getInt(
                    SharedPrefs.AlkalinityMeasure.key,
                    AlkalinityMeasure.DKH.code
                )
            )

            state = state.copy(
                plant = convertPlantMeasures.to(
                    state.plant,
                    state.alkalinityMeasureCode,
                    state.temperatureMeasureCode
                )
            )
        }
    }
}
