package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.info

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.screens.top_bar_menu.settings.SharedPrefs
import com.danilp.professionalaquarist.domain.dweller.Dweller
import com.danilp.professionalaquarist.domain.dweller.DwellerDataSource
import com.danilp.professionalaquarist.domain.dweller.use_case.ConvertDwellerMeasures
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DwellerInfoViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val convertDwellerMeasures: ConvertDwellerMeasures,
    private val dwellerDataSource: DwellerDataSource,
) : ViewModel() {

    var state by mutableStateOf(DwellerInfoState())

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<Long>("dwellerId") ?: return@launch

            state = state.copy(
                dweller = dwellerDataSource.getDwellerById(id) ?: Dweller.createEmpty()
            )

            val sharedPreferences = context.getSharedPreferences(
                SharedPrefs.InAquariumInfo.key,
                Context.MODE_PRIVATE
            )

            state = state.copy(
                dweller = state.dweller.copy(
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
                ),
                capacityMeasureCode = sharedPreferences.getInt(
                    SharedPrefs.CapacityMeasure.key,
                    CapacityMeasure.Liters.code
                )
            )

            state = state.copy(
                dweller = convertDwellerMeasures.to(
                    state.dweller,
                    state.alkalinityMeasureCode,
                    state.capacityMeasureCode,
                    state.temperatureMeasureCode
                )
            )
        }
    }
}
