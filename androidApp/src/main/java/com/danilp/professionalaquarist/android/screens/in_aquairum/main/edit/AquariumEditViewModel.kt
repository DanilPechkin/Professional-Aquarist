package com.danilp.professionalaquarist.android.screens.in_aquairum.main.edit

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
class AquariumEditViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val validate: Validate,
    private val aquariumDataSource: AquariumDataSource,
    private val convertAquariumMeasures: ConvertAquariumMeasures
) : ViewModel() {

    var state by mutableStateOf(AquariumEditState())

    private val completionEventChannel = Channel<CompletionEvent>()
    val completionEvents = completionEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences(
                SharedPrefs.InAquariumInfo.key,
                Context.MODE_PRIVATE
            )

            state = state.copy(
                aquarium = aquariumDataSource.getAquariumById(
                    sharedPreferences.getLong(
                        SharedPrefs.CurrentAquarium.key,
                        -1
                    )
                ) ?: Aquarium.createEmpty(),
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
                aquarium = convertAquariumMeasures.to(
                    state.aquarium,
                    state.alkalinityMeasureCode,
                    state.capacityMeasureCode,
                    state.temperatureMeasureCode
                )
            )

            state = state.copy(
                name = state.aquarium.name ?: "",
                liters = (state.aquarium.liters ?: "").toString(),
                description = state.aquarium.description ?: "",
                minTemperature = (state.aquarium.minTemperature ?: "").toString(),
                maxTemperature = (state.aquarium.maxTemperature ?: "").toString(),
                currentTemperature = (state.aquarium.currentTemperature ?: "").toString(),
                minCO2 = (state.aquarium.minCO2 ?: "").toString(),
                currentCO2 = (state.aquarium.currentCO2 ?: "").toString(),
                minIllumination = (state.aquarium.minIllumination ?: "").toString(),
                currentIllumination = (state.aquarium.currentIllumination ?: "").toString(),
                minPh = (state.aquarium.minPh ?: "").toString(),
                maxPh = (state.aquarium.maxPh ?: "").toString(),
                currentPh = (state.aquarium.currentPh ?: "").toString(),
                minGh = (state.aquarium.minGh ?: "").toString(),
                maxGh = (state.aquarium.maxGh ?: "").toString(),
                currentGh = (state.aquarium.currentGh ?: "").toString(),
                minKh = (state.aquarium.minKh ?: "").toString(),
                maxKh = (state.aquarium.maxKh ?: "").toString(),
                currentKh = (state.aquarium.currentKh ?: "").toString(),
                minK = (state.aquarium.minK ?: "").toString(),
                maxK = (state.aquarium.maxK ?: "").toString(),
                currentK = (state.aquarium.currentK ?: "").toString(),
                minMg = (state.aquarium.minMg ?: "").toString(),
                maxMg = (state.aquarium.maxMg ?: "").toString(),
                currentMg = (state.aquarium.currentMg ?: "").toString(),
                minFe = (state.aquarium.minFe ?: "").toString(),
                maxFe = (state.aquarium.maxFe ?: "").toString(),
                currentFe = (state.aquarium.currentFe ?: "").toString(),
                minAmmonia = (state.aquarium.minAmmonia ?: "").toString(),
                maxAmmonia = (state.aquarium.maxAmmonia ?: "").toString(),
                currentAmmonia = (state.aquarium.currentAmmonia ?: "").toString(),
                minCa = (state.aquarium.minCa ?: "").toString(),
                maxCa = (state.aquarium.maxCa ?: "").toString(),
                currentCa = (state.aquarium.currentCa ?: "").toString(),
                minPO4 = (state.aquarium.minPO4 ?: "").toString(),
                maxPO4 = (state.aquarium.maxPO4 ?: "").toString(),
                currentPO4 = (state.aquarium.currentPO4 ?: "").toString(),
                minNO3 = (state.aquarium.minNO3 ?: "").toString(),
                maxNO3 = (state.aquarium.maxNO3 ?: "").toString(),
                currentNO3 = (state.aquarium.currentNO3 ?: "").toString(),
                currentTags = state.aquarium.currentTags ?: listOf(),
                requiredTags = state.aquarium.requiredTags ?: listOf()
            )
        }
    }

    fun onEvent(event: AquariumEditEvent) {
        when (event) {
            is AquariumEditEvent.InsertButtonPressed -> {
                submitData()
            }

            is AquariumEditEvent.DeleteButtonPressed -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state = state.copy(isLoading = true)
                    delete(state.aquarium.id!!)
                    completionEventChannel.send(CompletionEvent.Delete)
                }
            }

            is AquariumEditEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }

            is AquariumEditEvent.LitersChanged -> {
                state = state.copy(liters = event.liters)
            }

            is AquariumEditEvent.DescriptionChanged -> {
                state = state.copy(description = event.description)
            }

            is AquariumEditEvent.ImagePicked -> {
                state = state.copy(
                    aquarium = state.aquarium.copy(imageUrl = event.imageUri)
                )
            }

            is AquariumEditEvent.CurrentTagSelected -> {
                state = state.copy(
                    currentTags = if (state.currentTags.contains(event.tag)) {
                        state.currentTags.filter { it != event.tag }
                    } else {
                        state.currentTags + event.tag
                    }
                )
            }
        }
    }

    private fun insert(aquarium: Aquarium) = viewModelScope.launch {
        aquariumDataSource.insertAquarium(aquarium)
        aquariumDataSource.refreshAquariumById(aquarium.id!!)
    }

    private fun delete(aquariumId: Long) = viewModelScope.launch {
        aquariumDataSource.deleteAquariumById(aquariumId)
    }

    private fun submitData() {
        viewModelScope.launch(Dispatchers.IO) {

            state = state.copy(isLoading = true)

            val nameResult = validate.string(state.name)
            val litersResult = validate.decimal(state.liters, isRequired = true)

            val hasError = listOf(
                nameResult,
                litersResult
            ).any { it.errorCode != null }

            if (hasError) {
                state = state.copy(
                    nameErrorCode = nameResult.errorCode,
                    litersErrorCode = litersResult.errorCode
                )
                return@launch
            }

            state = state.copy(
                aquarium = state.aquarium.copy(
                    name = state.name,
                    liters = state.liters.toDouble(),
                    description = state.description.ifBlank { null },
                    currentTags = state.currentTags.ifEmpty { null }
                )
            )

            state = state.copy(
                aquarium = convertAquariumMeasures.from(
                    state.aquarium,
                    state.alkalinityMeasureCode,
                    state.capacityMeasureCode,
                    state.temperatureMeasureCode
                )
            )

            insert(state.aquarium)
            completionEventChannel.send(CompletionEvent.Save)
            state = state.copy(isLoading = false)
        }
    }

    sealed class CompletionEvent {
        object Save : CompletionEvent()
        object Delete : CompletionEvent()
    }
}
