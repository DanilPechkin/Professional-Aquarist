package com.danilp.professionalaquarist.android.screens.aquarium.create

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.screens.top_bar_menu.settings.SharedPrefs
import com.danilp.professionalaquarist.domain.aquarium.Aquarium
import com.danilp.professionalaquarist.domain.aquarium.AquariumDataSource
import com.danilp.professionalaquarist.domain.aquarium.use_case.ConvertAquariumMeasures
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure
import com.danilp.professionalaquarist.domain.use_case.validation.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AquariumCreateViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val validate: Validate,
    private val aquariumDataSource: AquariumDataSource,
    private val convertAquariumMeasures: ConvertAquariumMeasures
) : ViewModel() {

    var state by mutableStateOf(AquariumCreateState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences(
                SharedPrefs.InAquariumInfo.key,
                Context.MODE_PRIVATE
            )

            state = state.copy(
                capacityMeasureCode = sharedPreferences.getInt(
                    SharedPrefs.CapacityMeasure.key,
                    CapacityMeasure.Liters.code
                )
            )
        }
    }

    fun onEvent(event: AquariumCreateEvent) {
        when (event) {
            is AquariumCreateEvent.InsertButtonPressed -> {
                submitData()
            }

            is AquariumCreateEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }

            is AquariumCreateEvent.LitersChanged -> {
                state = state.copy(liters = event.liters)
            }

            is AquariumCreateEvent.DescriptionChanged -> {
                state = state.copy(description = event.description)
            }

            is AquariumCreateEvent.ImagePicked -> {
                state = state.copy(
                    aquarium = state.aquarium.copy(imageUrl = event.imageUri)
                )
            }

            is AquariumCreateEvent.CurrentIlluminationChanged -> {
                state = state.copy(currentIllumination = event.currentIllumination)
            }
        }
    }

    private fun insert(aquarium: Aquarium) = viewModelScope.launch {
        aquariumDataSource.insertAquarium(aquarium)
    }

    private fun submitData() {
        state = state.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val nameResult = validate.string(state.name)
            val litersResult = validate.decimal(state.liters, isRequired = true)
            val currentIlluminationResult = validate.decimal(state.currentIllumination)

            val hasError = listOf(
                nameResult,
                litersResult,
                currentIlluminationResult
            ).any { it.errorCode != null }

            if (hasError) {
                state = state.copy(
                    nameErrorCode = nameResult.errorCode,
                    litersErrorCode = litersResult.errorCode,
                    currentIlluminationErrorCode = currentIlluminationResult.errorCode,
                )
                return@launch
            }

            state = state.copy(
                aquarium = state.aquarium.copy(
                    name = state.name,
                    liters = state.liters.toDouble(),
                    currentIllumination = state.currentIllumination.ifBlank { null }?.toDouble(),
                    description = state.description.ifBlank { null }
                )
            )

            state = state.copy(
                aquarium = convertAquariumMeasures.from(
                    state.aquarium,
                    AlkalinityMeasure.DKH.code,
                    state.capacityMeasureCode,
                    TemperatureMeasure.Celsius.code
                )
            )

            insert(state.aquarium)
            validationEventChannel.send(ValidationEvent.Success)
        }
        state = state.copy(isLoading = false)
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}
