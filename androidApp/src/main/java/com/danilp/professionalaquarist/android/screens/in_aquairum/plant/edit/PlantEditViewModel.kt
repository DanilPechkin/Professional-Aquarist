package com.danilp.professionalaquarist.android.screens.in_aquairum.plant.edit

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.domain.plant.Plant
import com.danilp.professionalaquarist.domain.plant.PlantDataSource
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.ConvertDKH
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.ConvertCelsius
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure
import com.danilp.professionalaquarist.domain.use_case.validation.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantEditViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val validate: Validate,
    private val convertCelsius: ConvertCelsius,
    private val convertDKH: ConvertDKH,
    private val plantDataSource: PlantDataSource
) : ViewModel() {

    var state by mutableStateOf(PlantEditState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<Long>("plantId") ?: return@launch

            state = state.copy(
                plant = if (id == -1L) Plant.createEmpty() else
                    plantDataSource.getPlantById(id) ?: Plant.createEmpty()
            )

            val sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.in_aquarium_info_shared_preferences_key),
                Context.MODE_PRIVATE
            )
            state = state.copy(
                plant = state.plant.copy(
                    aquariumId = sharedPreferences.getLong(
                        context.getString(R.string.saved_aquarium_id_key),
                        0
                    )
                ),
                temperatureMeasureCode = sharedPreferences.getInt(
                    context.getString(R.string.temperature_measure_id_key),
                    TemperatureMeasure.Celsius.code
                ),
                alkalinityMeasureCode = sharedPreferences.getInt(
                    context.getString(R.string.alkalinity_measure_id_key),
                    AlkalinityMeasure.DKH.code
                )
            )

            state = state.copy(
                name = state.plant.name ?: "",
                genus = state.plant.genus ?: "",
                minTemperature = if (state.plant.minTemperature == null) "" else
                    convertCelsius.to(
                        state.temperatureMeasureCode,
                        state.plant.minTemperature!!
                    ).result.toString(),
                maxTemperature = if (state.plant.maxTemperature == null) "" else
                    convertCelsius.to(
                        state.temperatureMeasureCode,
                        state.plant.maxTemperature!!
                    ).result.toString(),
                minPh = if (state.plant.minPh == null) "" else
                    convertDKH.to(
                        state.alkalinityMeasureCode,
                        state.plant.minPh!!
                    ).result.toString(),
                maxPh = if (state.plant.maxPh == null) "" else
                    convertDKH.to(
                        state.alkalinityMeasureCode,
                        state.plant.maxPh!!
                    ).result.toString(),
                minGh = if (state.plant.minGh == null) "" else
                    convertDKH.to(
                        state.alkalinityMeasureCode,
                        state.plant.minGh!!
                    ).result.toString(),
                maxGh = if (state.plant.maxGh == null) "" else
                    convertDKH.to(
                        state.alkalinityMeasureCode,
                        state.plant.maxGh!!
                    ).result.toString(),
                minKh = if (state.plant.minKh == null) "" else
                    convertDKH.to(
                        state.alkalinityMeasureCode,
                        state.plant.minKh!!
                    ).result.toString(),
                maxKh = if (state.plant.maxKh == null) "" else
                    convertDKH.to(
                        state.alkalinityMeasureCode,
                        state.plant.maxKh!!
                    ).result.toString(),
                minCO2 = if (state.plant.minCO2 == null) "" else state.plant.minCO2.toString(),
                minIllumination = if (state.plant.minIllumination == null) "" else
                    state.plant.minIllumination.toString(),
                description = state.plant.description ?: ""
            )
        }
    }

    fun onEvent(event: PlantEditEvent) {
        when (event) {
            is PlantEditEvent.InsertButtonPressed -> {
                submitData()
            }

            is PlantEditEvent.DeleteButtonPressed -> {
                viewModelScope.launch {
                    if (state.plant.id != null)
                        delete(state.plant.id!!)
                    validationEventChannel.send(ValidationEvent.Success)
                }
            }

            is PlantEditEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }

            is PlantEditEvent.GenusChanged -> {
                state = state.copy(genus = event.genus)
            }

            is PlantEditEvent.MinTemperatureChanged -> {
                state = state.copy(minTemperature = event.temp)
            }

            is PlantEditEvent.MaxTemperatureChanged -> {
                state = state.copy(maxTemperature = event.temp)
            }

            is PlantEditEvent.MinPhChanged -> {
                state = state.copy(minPh = event.ph)
            }

            is PlantEditEvent.MaxPhChanged -> {
                state = state.copy(maxPh = event.ph)
            }

            is PlantEditEvent.MinGhChanged -> {
                state = state.copy(minGh = event.gh)
            }

            is PlantEditEvent.MaxGhChanged -> {
                state = state.copy(maxGh = event.gh)
            }

            is PlantEditEvent.MinKhChanged -> {
                state = state.copy(minKh = event.kh)
            }

            is PlantEditEvent.MaxKhChanged -> {
                state = state.copy(maxKh = event.kh)
            }

            is PlantEditEvent.MinCO2Changed -> {
                state = state.copy(minCO2 = event.co2)
            }

            is PlantEditEvent.MinIlluminationChanged -> {
                state = state.copy(minIllumination = event.illumination)
            }

            is PlantEditEvent.DescriptionChanged -> {
                state = state.copy(description = event.description)
            }

            is PlantEditEvent.ImagePicked -> {
                state = state.copy(
                    plant = state.plant.copy(imageUrl = event.imageUrl)
                )
            }
        }
    }

    private fun insert(plant: Plant) = viewModelScope.launch {
        plantDataSource.insertPlant(plant)
    }

    private fun delete(plantId: Long) = viewModelScope.launch {
        plantDataSource.deletePlantById(plantId)
    }

    private fun submitData() {
        val nameResult = validate.string(state.name)
        val minTemperatureResult = validate.decimal(state.minTemperature, isRequired = true)
        val maxTemperatureResult = validate.decimal(state.maxTemperature, isRequired = true)
        val minPhResult = validate.decimal(state.minPh)
        val maxPhResult = validate.decimal(state.maxPh)
        val minGhResult = validate.decimal(state.minGh)
        val maxGhResult = validate.decimal(state.maxGh)
        val minKhResult = validate.decimal(state.minKh)
        val maxKhResult = validate.decimal(state.maxKh)
        val minCO2Result = validate.decimal(state.minCO2)
        val minIlluminationResult = validate.decimal(state.minIllumination)

        val hasError = listOf(
            nameResult,
            minTemperatureResult,
            maxTemperatureResult,
            minPhResult,
            maxPhResult,
            minGhResult,
            maxGhResult,
            minKhResult,
            maxKhResult,
            minCO2Result,
            minIlluminationResult
        ).any { it.errorCode != null }

        if (hasError) {
            state = state.copy(
                nameErrorCode = nameResult.errorCode,
                minTemperatureErrorCode = minTemperatureResult.errorCode,
                maxTemperatureErrorCode = maxTemperatureResult.errorCode,
                minPhErrorCode = minPhResult.errorCode,
                maxPhErrorCode = maxPhResult.errorCode,
                minGhErrorCode = minGhResult.errorCode,
                maxGhErrorCode = maxGhResult.errorCode,
                minKhErrorCode = minKhResult.errorCode,
                maxKhErrorCode = maxKhResult.errorCode,
                minCO2ErrorCode = minCO2Result.errorCode,
                minIlluminationErrorCode = minIlluminationResult.errorCode
            )
            return
        }

        viewModelScope.launch {
            val isTempCorrect = (state.minTemperature.toDouble() < state.maxTemperature.toDouble())
            val isPhCorrect = (((state.minPh.toDoubleOrNull()
                ?: 0.0) < (state.maxPh.toDoubleOrNull() ?: 0.0)))
            val isGhCorrect = (((state.minGh.toDoubleOrNull()
                ?: 0.0) < (state.maxGh.toDoubleOrNull() ?: 0.0)))
            val isKhCorrect = (((state.minKh.toDoubleOrNull()
                ?: 0.0) < (state.maxKh.toDoubleOrNull() ?: 0.0)))

            if (!isTempCorrect) {
                kotlin.run {
                    val temp = state.minTemperature
                    state = state.copy(minTemperature = state.maxTemperature)
                    state = state.copy(maxTemperature = temp)
                }
            }

            if (!isPhCorrect) {
                kotlin.run {
                    val temp = state.minPh
                    state = state.copy(minPh = state.maxPh)
                    state = state.copy(maxPh = temp)
                }
            }

            if (!isGhCorrect) {
                kotlin.run {
                    val temp = state.minGh
                    state = state.copy(minGh = state.maxGh)
                    state = state.copy(maxGh = temp)
                }
            }

            if (!isKhCorrect) {
                kotlin.run {
                    val temp = state.minKh
                    state = state.copy(minKh = state.maxKh)
                    state = state.copy(maxKh = temp)
                }
            }

            state = state.copy(
                plant = state.plant.copy(
                    name = state.name.ifBlank { null },
                    genus = state.genus.ifBlank { null },
                    minTemperature = if (state.minTemperature.isBlank()) null
                    else convertCelsius.from(
                        state.temperatureMeasureCode,
                        state.minTemperature.toDoubleOrNull() ?: 0.0
                    ).result,
                    maxTemperature = if (state.maxTemperature.isBlank()) null
                    else convertCelsius.from(
                        state.temperatureMeasureCode,
                        state.maxTemperature.toDoubleOrNull() ?: 0.0
                    ).result,
                    minPh = if (state.minPh.isBlank()) null
                    else convertDKH.from(
                        state.alkalinityMeasureCode,
                        state.minPh.toDoubleOrNull() ?: 0.0
                    ).result,
                    maxPh = if (state.maxPh.isBlank()) null
                    else convertDKH.from(
                        state.alkalinityMeasureCode,
                        state.maxPh.toDoubleOrNull() ?: 0.0
                    ).result,
                    minGh = if (state.minGh.isBlank()) null
                    else convertDKH.from(
                        state.alkalinityMeasureCode,
                        state.minGh.toDoubleOrNull() ?: 0.0
                    ).result,
                    maxGh = if (state.maxGh.isBlank()) null
                    else convertDKH.from(
                        state.alkalinityMeasureCode,
                        state.maxGh.toDoubleOrNull() ?: 0.0
                    ).result,
                    minKh = if (state.minKh.isBlank()) null
                    else convertDKH.from(
                        state.alkalinityMeasureCode,
                        state.minKh.toDoubleOrNull() ?: 0.0
                    ).result,
                    maxKh = if (state.maxKh.isBlank()) null
                    else convertDKH.from(
                        state.alkalinityMeasureCode,
                        state.maxKh.toDoubleOrNull() ?: 0.0
                    ).result,
                    minIllumination = state.minIllumination.ifBlank { null }?.toDoubleOrNull(),
                    minCO2 = state.minCO2.ifBlank { null }?.toDoubleOrNull(),
                    description = state.description.ifBlank { null }
                )
            )

            insert(state.plant)
            validationEventChannel.send(ValidationEvent.Success)
        }

    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}