package com.danilp.professionalaquarist.android.screens.in_aquairum.plant.edit

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.screens.top_bar_menu.settings.SharedPrefs
import com.danilp.professionalaquarist.domain.aquarium.AquariumDataSource
import com.danilp.professionalaquarist.domain.plant.Plant
import com.danilp.professionalaquarist.domain.plant.PlantDataSource
import com.danilp.professionalaquarist.domain.plant.tags.PlantTags
import com.danilp.professionalaquarist.domain.plant.use_case.ConvertPlantMeasures
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
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
class PlantEditViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val validate: Validate,
    private val plantDataSource: PlantDataSource,
    private val convertPlantMeasures: ConvertPlantMeasures,
    private val aquariumDataSource: AquariumDataSource
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

            state = state.copy(
                name = state.plant.name ?: "",
                genus = state.plant.genus ?: "",
                minTemperature = (state.plant.minTemperature ?: "").toString(),
                maxTemperature = (state.plant.maxTemperature ?: "").toString(),
                minPh = (state.plant.minPh ?: "").toString(),
                maxPh = (state.plant.maxPh ?: "").toString(),
                minGh = (state.plant.minGh ?: "").toString(),
                maxGh = (state.plant.maxGh ?: "").toString(),
                minKh = (state.plant.minKh ?: "").toString(),
                maxKh = (state.plant.maxKh ?: "").toString(),
                minCO2 = (state.plant.minCO2 ?: "").toString(),
                minIllumination = (state.plant.minIllumination ?: "").toString(),
                description = state.plant.description ?: "",
                typeTag = state.plant.tags?.find { tag ->
                    listOf(
                        PlantTags.BROADLEAF_PLANT.code,
                        PlantTags.FLOATING_PLANT.code,
                        PlantTags.FERN.code,
                        PlantTags.MOSS.code,
                        PlantTags.LONG_STEMMED_PLANT.code
                    ).contains(tag)
                } ?: "",
                tags = state.plant.tags ?: listOf()
            )
        }
    }

    fun onEvent(event: PlantEditEvent) {
        when (event) {
            is PlantEditEvent.InsertButtonPressed -> {
                submitData()
            }

            is PlantEditEvent.DeleteButtonPressed -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state = state.copy(isLoading = true)
                    if (state.plant.id != null)
                        delete(state.plant.id!!)
                    validationEventChannel.send(ValidationEvent.Success)
                }
            }

            is PlantEditEvent.NameChanged -> {
                state = state.copy(name = event.name, nameErrorCode = null)
            }

            is PlantEditEvent.GenusChanged -> {
                state = state.copy(genus = event.genus)
            }

            is PlantEditEvent.MinTemperatureChanged -> {
                state = state.copy(
                    minTemperature = event.temp,
                    minTemperatureErrorCode = null
                )
            }

            is PlantEditEvent.MaxTemperatureChanged -> {
                state = state.copy(
                    maxTemperature = event.temp,
                    maxTemperatureErrorCode = null
                )
            }

            is PlantEditEvent.MinPhChanged -> {
                state = state.copy(minPh = event.ph, minPhErrorCode = null)
            }

            is PlantEditEvent.MaxPhChanged -> {
                state = state.copy(maxPh = event.ph, maxPhErrorCode = null)
            }

            is PlantEditEvent.MinGhChanged -> {
                state = state.copy(minGh = event.gh, minGhErrorCode = null)
            }

            is PlantEditEvent.MaxGhChanged -> {
                state = state.copy(maxGh = event.gh, maxGhErrorCode = null)
            }

            is PlantEditEvent.MinKhChanged -> {
                state = state.copy(minKh = event.kh, minKhErrorCode = null)
            }

            is PlantEditEvent.MaxKhChanged -> {
                state = state.copy(maxKh = event.kh, maxKhErrorCode = null)
            }

            is PlantEditEvent.MinCO2Changed -> {
                state = state.copy(minCO2 = event.co2, minCO2ErrorCode = null)
            }

            is PlantEditEvent.MinIlluminationChanged -> {
                state = state.copy(
                    minIllumination = event.illumination,
                    minIlluminationErrorCode = null
                )
            }

            is PlantEditEvent.DescriptionChanged -> {
                state = state.copy(description = event.description)
            }

            is PlantEditEvent.ImagePicked -> {
                state = state.copy(
                    plant = state.plant.copy(imageUrl = event.imageUrl)
                )
            }

            is PlantEditEvent.TagSelected -> {
                state = state.copy(
                    tags = if (state.tags.contains(event.tag)) {
                        state.tags.filter { it != event.tag }
                    } else {
                        state.tags + event.tag
                    }
                )
            }

            is PlantEditEvent.TypeTagSelected -> {
                state = state.copy(typeTag = event.typeTag, typeTagErrorCode = null)
            }
        }
    }

    private fun insert(plant: Plant) = viewModelScope.launch {
        plantDataSource.insertPlant(plant)
        refreshAquarium(state.plant.aquariumId)
    }

    private fun delete(plantId: Long) = viewModelScope.launch {
        plantDataSource.deletePlantById(plantId)
        refreshAquarium(state.plant.aquariumId)
    }

    private fun refreshAquarium(aquariumId: Long) = viewModelScope.launch {
        aquariumDataSource.refreshAquariumById(aquariumId)
    }

    private fun submitData() {
        viewModelScope.launch(Dispatchers.IO) {

            state = state.copy(isLoading = true)

            val nameResult = validate.string(state.name)
            val minTemperatureResult = validate.decimal(state.minTemperature)
            val maxTemperatureResult = validate.decimal(state.maxTemperature)
            val minPhResult = validate.decimal(state.minPh)
            val maxPhResult = validate.decimal(state.maxPh)
            val minGhResult = validate.decimal(state.minGh)
            val maxGhResult = validate.decimal(state.maxGh)
            val minKhResult = validate.decimal(state.minKh)
            val maxKhResult = validate.decimal(state.maxKh)
            val minCO2Result = validate.decimal(state.minCO2)
            val minIlluminationResult = validate.decimal(state.minIllumination)
            val typeTagResult = validate.string(state.typeTag)

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
                minIlluminationResult,
                typeTagResult
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
                    minIlluminationErrorCode = minIlluminationResult.errorCode,
                    typeTagErrorCode = typeTagResult.errorCode
                )
                return@launch
            }

            state = state.copy(
                plant = state.plant.copy(
                    name = state.name.ifBlank { null },
                    genus = state.genus.ifBlank { null },
                    minTemperature = state.minTemperature.ifBlank { null }?.toDouble(),
                    maxTemperature = state.maxTemperature.ifBlank { null }?.toDouble(),
                    minPh = state.minPh.ifBlank { null }?.toDouble(),
                    maxPh = state.maxPh.ifBlank { null }?.toDouble(),
                    minGh = state.minGh.ifBlank { null }?.toDouble(),
                    maxGh = state.maxGh.ifBlank { null }?.toDouble(),
                    minKh = state.minKh.ifBlank { null }?.toDouble(),
                    maxKh = state.maxKh.ifBlank { null }?.toDouble(),
                    minIllumination = state.minIllumination.ifBlank { null }?.toDoubleOrNull(),
                    minCO2 = state.minCO2.ifBlank { null }?.toDoubleOrNull(),
                    description = state.description.ifBlank { null },
                    tags = (
                        state.tags.filter { tag ->
                            !listOf(
                                PlantTags.BROADLEAF_PLANT.code,
                                PlantTags.FLOATING_PLANT.code,
                                PlantTags.FERN.code,
                                PlantTags.MOSS.code,
                                PlantTags.LONG_STEMMED_PLANT.code
                            ).contains(tag)
                        } + listOf(state.typeTag)
                        ).ifEmpty { null },
                )
            )

            state = state.copy(
                plant = convertPlantMeasures.from(
                    state.plant,
                    state.alkalinityMeasureCode,
                    state.temperatureMeasureCode
                )
            )

            insert(state.plant)
            validationEventChannel.send(ValidationEvent.Success)
            state = state.copy(isLoading = false)
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}
