package com.danilp.professionalaquarist.android.screens.aquarium.edit

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AquariumEditViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val validate: Validate,
    private val aquariumDataSource: AquariumDataSource,
    private val convertAquariumMeasures: ConvertAquariumMeasures
) : ViewModel() {

    var state by mutableStateOf(AquariumEditState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<Long>("aquariumId") ?: return@launch
            state = state.copy(
                aquarium = if (id == -1L) Aquarium.createEmpty() else
                    aquariumDataSource.getAquariumById(id) ?: Aquarium.createEmpty()
            )

            val sharedPreferences = context.getSharedPreferences(
                SharedPrefs.InAquariumInfo.key,
                Context.MODE_PRIVATE
            )

            state = state.copy(
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
                minCO2 = (state.aquarium.minCO2 ?: "").toString(),
                minIllumination = (state.aquarium.minIllumination ?: "").toString(),
                minPh = (state.aquarium.minPh ?: "").toString(),
                maxPh = (state.aquarium.maxPh ?: "").toString(),
                minGh = (state.aquarium.minGh ?: "").toString(),
                maxGh = (state.aquarium.maxGh ?: "").toString(),
                minKh = (state.aquarium.minKh ?: "").toString(),
                maxKh = (state.aquarium.maxKh ?: "").toString(),
                minK = (state.aquarium.minK ?: "").toString(),
                maxK = (state.aquarium.maxK ?: "").toString(),
                minMg = (state.aquarium.minMg ?: "").toString(),
                maxMg = (state.aquarium.maxMg ?: "").toString(),
                minFe = (state.aquarium.minFe ?: "").toString(),
                maxFe = (state.aquarium.maxFe ?: "").toString(),
                minAmmonia = (state.aquarium.minAmmonia ?: "").toString(),
                maxAmmonia = (state.aquarium.maxAmmonia ?: "").toString(),
                minCa = (state.aquarium.minCa ?: "").toString(),
                maxCa = (state.aquarium.maxCa ?: "").toString(),
                minPO4 = (state.aquarium.minPO4 ?: "").toString(),
                maxPO4 = (state.aquarium.maxPO4 ?: "").toString(),
                minNO3 = (state.aquarium.minNO3 ?: "").toString(),
                maxNO3 = (state.aquarium.maxNO3 ?: "").toString()
            )
        }
    }

    fun onEvent(event: AquariumEditEvent) {
        when (event) {
            is AquariumEditEvent.InsertButtonPressed -> {
                submitData()
            }

            is AquariumEditEvent.DeleteButtonPressed -> {
                viewModelScope.launch {
                    if (state.aquarium.id != null) delete(state.aquarium.id!!)
                    validationEventChannel.send(ValidationEvent.Success)
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

            is AquariumEditEvent.MaxAmmoniaChanged -> {
                state = state.copy(maxAmmonia = event.ammonia)
            }

            is AquariumEditEvent.MaxCaChanged -> {
                state = state.copy(maxCa = event.ca)
            }

            is AquariumEditEvent.MaxFeChanged -> {
                state = state.copy(maxFe = event.fe)
            }

            is AquariumEditEvent.MaxGhChanged -> {
                state = state.copy(maxGh = event.gh)
            }

            is AquariumEditEvent.MaxKChanged -> {
                state = state.copy(maxK = event.k)
            }

            is AquariumEditEvent.MaxKhChanged -> {
                state = state.copy(maxKh = event.kh)
            }

            is AquariumEditEvent.MaxMgChanged -> {
                state = state.copy(maxMg = event.mg)
            }

            is AquariumEditEvent.MaxNO3Changed -> {
                state = state.copy(maxNO3 = event.no3)
            }

            is AquariumEditEvent.MaxPO4Changed -> {
                state = state.copy(maxPO4 = event.po4)
            }

            is AquariumEditEvent.MaxPhChanged -> {
                state = state.copy(maxPh = event.ph)
            }

            is AquariumEditEvent.MaxTemperatureChanged -> {
                state = state.copy(maxTemperature = event.temperature)
            }

            is AquariumEditEvent.MinAmmoniaChanged -> {
                state = state.copy(minAmmonia = event.ammonia)
            }

            is AquariumEditEvent.MinCaChanged -> {
                state = state.copy(minCa = event.ca)
            }

            is AquariumEditEvent.MinFeChanged -> {
                state = state.copy(minFe = event.fe)
            }

            is AquariumEditEvent.MinGhChanged -> {
                state = state.copy(minGh = event.gh)
            }

            is AquariumEditEvent.MinKChanged -> {
                state = state.copy(minK = event.k)
            }

            is AquariumEditEvent.MinKhChanged -> {
                state = state.copy(minKh = event.kh)
            }

            is AquariumEditEvent.MinMgChanged -> {
                state = state.copy(minMg = event.mg)
            }

            is AquariumEditEvent.MinNO3Changed -> {
                state = state.copy(minNO3 = event.no3)
            }

            is AquariumEditEvent.MinPO4Changed -> {
                state = state.copy(minPO4 = event.po4)
            }

            is AquariumEditEvent.MinPhChanged -> {
                state = state.copy(minPh = event.ph)
            }

            is AquariumEditEvent.MinTemperatureChanged -> {
                state = state.copy(minTemperature = event.temperature)
            }

            is AquariumEditEvent.MinCO2Changed -> {
                state = state.copy(minCO2 = event.co2)
            }

            is AquariumEditEvent.MinIlluminationChanged -> {
                state = state.copy(minIllumination = event.illumination)
            }
        }
    }

    private fun insert(aquarium: Aquarium) = viewModelScope.launch {
        aquariumDataSource.insertAquarium(aquarium)
    }

    private fun delete(aquariumId: Long) = viewModelScope.launch {
        aquariumDataSource.deleteAquariumById(aquariumId)
    }

    private fun submitData() {
        val nameResult = validate.string(state.name)
        val litersResult = validate.decimal(state.liters, isRequired = true)
        val minTemperatureResult = validate.decimal(state.minTemperature)
        val maxTemperatureResult = validate.decimal(state.maxTemperature)
        val minPhResult = validate.decimal(state.minPh)
        val maxPhResult = validate.decimal(state.maxPh)
        val minGhResult = validate.decimal(state.minGh)
        val maxGhResult = validate.decimal(state.maxGh)
        val minKhResult = validate.decimal(state.minKh)
        val maxKhResult = validate.decimal(state.maxKh)
        val minAmmoniaResult = validate.decimal(state.minAmmonia)
        val maxAmmoniaResult = validate.decimal(state.maxAmmonia)
        val minKResult = validate.decimal(state.minK)
        val maxKResult = validate.decimal(state.maxK)
        val minCaResult = validate.decimal(state.minCa)
        val maxCaResult = validate.decimal(state.maxCa)
        val minFeResult = validate.decimal(state.minFe)
        val maxFeResult = validate.decimal(state.maxFe)
        val minMgResult = validate.decimal(state.minMg)
        val maxMgResult = validate.decimal(state.maxMg)
        val minPO4Result = validate.decimal(state.minPO4)
        val maxPO4Result = validate.decimal(state.maxPO4)
        val minNO3Result = validate.decimal(state.minNO3)
        val maxNO3Result = validate.decimal(state.maxNO3)
        val minIlluminationResult = validate.decimal(state.minIllumination)
        val minCO2Result = validate.decimal(state.minCO2)

        val hasError = listOf(
            nameResult,
            litersResult,
            minTemperatureResult,
            maxTemperatureResult,
            minPhResult,
            maxPhResult,
            minGhResult,
            maxGhResult,
            minKhResult,
            maxKhResult,
            minAmmoniaResult,
            maxAmmoniaResult,
            minKResult,
            maxKResult,
            minCaResult,
            maxCaResult,
            minFeResult,
            maxFeResult,
            minMgResult,
            maxMgResult,
            minPO4Result,
            maxPO4Result,
            minNO3Result,
            maxNO3Result,
            minIlluminationResult,
            minCO2Result
        ).any { it.errorCode != null }

        if (hasError) {
            state = state.copy(
                nameErrorCode = nameResult.errorCode,
                litersErrorCode = litersResult.errorCode,
                minTemperatureErrorCode = minTemperatureResult.errorCode,
                maxTemperatureErrorCode = maxTemperatureResult.errorCode,
                minPhErrorCode = minPhResult.errorCode,
                maxPhErrorCode = maxPhResult.errorCode,
                minGhErrorCode = minGhResult.errorCode,
                maxGhErrorCode = maxGhResult.errorCode,
                minKhErrorCode = minKhResult.errorCode,
                maxKhErrorCode = maxKhResult.errorCode,
                minKErrorCode = minKResult.errorCode,
                maxKErrorCode = maxKResult.errorCode,
                minCaErrorCode = minCaResult.errorCode,
                maxCaErrorCode = maxCaResult.errorCode,
                minFeErrorCode = minFeResult.errorCode,
                maxFeErrorCode = maxFeResult.errorCode,
                minMgErrorCode = minMgResult.errorCode,
                maxMgErrorCode = maxMgResult.errorCode,
                minPO4ErrorCode = minPO4Result.errorCode,
                maxPO4ErrorCode = maxPO4Result.errorCode,
                minNO3ErrorCode = minNO3Result.errorCode,
                maxNO3ErrorCode = maxNO3Result.errorCode,
                minAmmoniaErrorCode = minAmmoniaResult.errorCode,
                maxAmmoniaErrorCode = maxAmmoniaResult.errorCode,
                minIlluminationErrorCode = minIlluminationResult.errorCode,
                minCO2ErrorCode = minCO2Result.errorCode
            )
            return
        }

        viewModelScope.launch {
            val isTempCorrect =
                (
                        (state.minTemperature.toDoubleOrNull() ?: 0.0) <
                                (state.maxTemperature.toDoubleOrNull() ?: 0.0)
                        )
            val isPhCorrect =
                ((state.minPh.toDoubleOrNull() ?: 0.0) < (state.maxPh.toDoubleOrNull() ?: 0.0))
            val isGhCorrect =
                ((state.minGh.toDoubleOrNull() ?: 0.0) < (state.maxGh.toDoubleOrNull() ?: 0.0))
            val isKhCorrect =
                ((state.minKh.toDoubleOrNull() ?: 0.0) < (state.maxKh.toDoubleOrNull() ?: 0.0))
            val isKCorrect =
                ((state.minK.toDoubleOrNull() ?: 0.0) < (state.maxK.toDoubleOrNull() ?: 0.0))
            val isCaCorrect =
                ((state.minCa.toDoubleOrNull() ?: 0.0) < (state.maxCa.toDoubleOrNull() ?: 0.0))
            val isMgCorrect =
                ((state.minMg.toDoubleOrNull() ?: 0.0) < (state.maxMg.toDoubleOrNull() ?: 0.0))
            val isFeCorrect =
                ((state.minFe.toDoubleOrNull() ?: 0.0) < (state.maxFe.toDoubleOrNull() ?: 0.0))
            val isAmmoniaCorrect =
                (
                        (state.minAmmonia.toDoubleOrNull() ?: 0.0) <
                                (state.maxAmmonia.toDoubleOrNull() ?: 0.0)
                        )
            val isPO4Correct =
                ((state.minPO4.toDoubleOrNull() ?: 0.0) < (state.maxPO4.toDoubleOrNull() ?: 0.0))
            val isNO3Correct =
                ((state.minNO3.toDoubleOrNull() ?: 0.0) < (state.maxNO3.toDoubleOrNull() ?: 0.0))

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

            if (!isKCorrect) {
                kotlin.run {
                    val temp = state.minK
                    state = state.copy(minK = state.maxK)
                    state = state.copy(maxK = temp)
                }
            }

            if (!isCaCorrect) {
                kotlin.run {
                    val temp = state.minCa
                    state = state.copy(minCa = state.maxCa)
                    state = state.copy(maxCa = temp)
                }
            }

            if (!isMgCorrect) {
                kotlin.run {
                    val temp = state.minMg
                    state = state.copy(minMg = state.maxMg)
                    state = state.copy(maxMg = temp)
                }
            }

            if (!isFeCorrect) {
                kotlin.run {
                    val temp = state.minFe
                    state = state.copy(minFe = state.maxFe)
                    state = state.copy(maxFe = temp)
                }
            }

            if (!isAmmoniaCorrect) {
                kotlin.run {
                    val temp = state.minAmmonia
                    state = state.copy(minAmmonia = state.maxAmmonia)
                    state = state.copy(maxAmmonia = temp)
                }
            }

            if (!isPO4Correct) {
                kotlin.run {
                    val temp = state.minPO4
                    state = state.copy(minPO4 = state.maxPO4)
                    state = state.copy(maxPO4 = temp)
                }
            }

            if (!isNO3Correct) {
                kotlin.run {
                    val temp = state.minNO3
                    state = state.copy(minNO3 = state.maxNO3)
                    state = state.copy(maxNO3 = temp)
                }
            }

            state = state.copy(
                aquarium = state.aquarium.copy(
                    name = state.name,
                    liters = state.liters.toDouble(),
                    minTemperature = state.minTemperature.ifBlank { null }?.toDouble(),
                    maxTemperature = state.maxTemperature.ifBlank { null }?.toDouble(),
                    minIllumination = state.minIllumination.ifBlank { null }?.toDouble(),
                    minCO2 = state.minCO2.ifBlank { null }?.toDouble(),
                    minPh = state.minPh.ifBlank { null }?.toDouble(),
                    maxPh = state.maxPh.ifBlank { null }?.toDouble(),
                    minGh = state.minGh.ifBlank { null }?.toDouble(),
                    maxGh = state.maxGh.ifBlank { null }?.toDouble(),
                    minKh = state.minKh.ifBlank { null }?.toDouble(),
                    maxKh = state.maxKh.ifBlank { null }?.toDouble(),
                    minK = state.minK.ifBlank { null }?.toDouble(),
                    maxK = state.maxK.ifBlank { null }?.toDouble(),
                    minCa = state.minCa.ifBlank { null }?.toDouble(),
                    maxCa = state.maxCa.ifBlank { null }?.toDouble(),
                    minAmmonia = state.minAmmonia.ifBlank { null }?.toDouble(),
                    maxAmmonia = state.maxAmmonia.ifBlank { null }?.toDouble(),
                    minMg = state.minMg.ifBlank { null }?.toDouble(),
                    maxMg = state.maxMg.ifBlank { null }?.toDouble(),
                    minFe = state.minFe.ifBlank { null }?.toDouble(),
                    maxFe = state.maxFe.ifBlank { null }?.toDouble(),
                    minPO4 = state.minPO4.ifBlank { null }?.toDouble(),
                    maxPO4 = state.maxPO4.ifBlank { null }?.toDouble(),
                    minNO3 = state.minNO3.ifBlank { null }?.toDouble(),
                    maxNO3 = state.maxNO3.ifBlank { null }?.toDouble(),
                    description = state.description.ifBlank { null }
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
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}
