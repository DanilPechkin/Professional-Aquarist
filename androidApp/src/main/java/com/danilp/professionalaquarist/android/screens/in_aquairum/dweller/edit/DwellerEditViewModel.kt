package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.edit

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.domain.dweller.Dweller
import com.danilp.professionalaquarist.domain.dweller.DwellerDataSource
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.ConvertDKH
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.ConvertLiters
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
class DwellerEditViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val validate: Validate,
    private val convertCelsius: ConvertCelsius,
    private val convertLiters: ConvertLiters,
    private val convertDKH: ConvertDKH,
    private val dwellerDataSource: DwellerDataSource
) : ViewModel() {

    var state by mutableStateOf(DwellerEditState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<Long>("dwellerId") ?: return@launch

            state = state.copy(dweller = dwellerDataSource.getDwellerById(id)!!)

            val sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.in_aquarium_info_shared_preferences_key),
                Context.MODE_PRIVATE
            )

            state = state.copy(
                dweller = state.dweller.copy(
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
                ),
                capacityMeasureCode = sharedPreferences.getInt(
                    context.getString(R.string.capacity_measure_id_key),
                    CapacityMeasure.Liters.code
                )
            )

            state = state.copy(
                name = state.dweller.name,
                genus = state.dweller.genus,
                amount = if (state.dweller.amount == 0L) "" else state.dweller.amount.toString(),
                minTemperature = if (state.dweller.minTemperature == 0.0) "" else
                    convertCelsius.to(
                        state.temperatureMeasureCode,
                        state.dweller.minTemperature
                    ).result.toString(),
                maxTemperature = if (state.dweller.maxTemperature == 0.0) "" else
                    convertCelsius.to(
                        state.temperatureMeasureCode,
                        state.dweller.maxTemperature
                    ).result.toString(),
                liters = if (state.dweller.liters == 0.0) "" else
                    convertLiters.to(
                        state.capacityMeasureCode,
                        state.dweller.liters
                    ).result.toString(),
                minPh = if (state.dweller.minPh == 0.0) "" else
                    convertDKH.to(
                        state.alkalinityMeasureCode,
                        state.dweller.minPh
                    ).result.toString(),
                maxPh = if (state.dweller.maxPh == 0.0) "" else
                    convertDKH.to(
                        state.alkalinityMeasureCode,
                        state.dweller.maxPh
                    ).result.toString(),
                minGh = if (state.dweller.minGh == 0.0) "" else
                    convertDKH.to(
                        state.alkalinityMeasureCode,
                        state.dweller.minGh
                    ).result.toString(),
                maxGh = if (state.dweller.maxGh == 0.0) "" else
                    convertDKH.to(
                        state.alkalinityMeasureCode,
                        state.dweller.maxGh
                    ).result.toString(),
                minKh = if (state.dweller.minKh == 0.0) "" else
                    convertDKH.to(
                        state.alkalinityMeasureCode,
                        state.dweller.minKh
                    ).result.toString(),
                maxKh = if (state.dweller.maxKh == 0.0) "" else
                    convertDKH.to(
                        state.alkalinityMeasureCode,
                        state.dweller.maxKh
                    ).result.toString(),
                description = state.dweller.description
            )
        }
    }

    fun onEvent(event: DwellerEditEvent) {
        when (event) {
            is DwellerEditEvent.InsertButtonPressed -> {
                submitData()
            }

            is DwellerEditEvent.DeleteButtonPressed -> {
                viewModelScope.launch {
                    delete(state.dweller.id)
                    validationEventChannel.send(ValidationEvent.Success)
                }
            }

            is DwellerEditEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }

            is DwellerEditEvent.GenusChanged -> {
                state = state.copy(genus = event.genus)
            }

            is DwellerEditEvent.AmountChanged -> {
                state = state.copy(amount = event.amount)
            }

            is DwellerEditEvent.MinTemperatureChanged -> {
                state = state.copy(minTemperature = event.temp)
            }

            is DwellerEditEvent.MaxTemperatureChanged -> {
                state = state.copy(maxTemperature = event.temp)
            }

            is DwellerEditEvent.LitersChanged -> {
                state = state.copy(liters = event.liters)
            }

            is DwellerEditEvent.MinPhChanged -> {
                state = state.copy(minPh = event.ph)
            }

            is DwellerEditEvent.MaxPhChanged -> {
                state = state.copy(maxPh = event.ph)
            }

            is DwellerEditEvent.MinGhChanged -> {
                state = state.copy(minGh = event.gh)
            }

            is DwellerEditEvent.MaxGhChanged -> {
                state = state.copy(maxGh = event.gh)
            }

            is DwellerEditEvent.MinKhChanged -> {
                state = state.copy(minKh = event.kh)
            }

            is DwellerEditEvent.MaxKhChanged -> {
                state = state.copy(maxKh = event.kh)
            }

            is DwellerEditEvent.DescriptionChanged -> {
                state = state.copy(description = event.description)
            }

            is DwellerEditEvent.ImagePicked -> {
                state = state.copy(
                    dweller = state.dweller.copy(imageUrl = event.imageUrl)
                )
            }
        }
    }

    private fun insert(dweller: Dweller) = viewModelScope.launch {
        dwellerDataSource.insertDweller(dweller)
    }

    private fun delete(dwellerId: Long) = viewModelScope.launch {
        dwellerDataSource.deleteDwellerById(dwellerId)
    }

    private fun submitData() {
        val nameResult = validate.string(state.name)
        val amountResult = validate.integer(state.amount, isRequired = true)
        val minTemperatureResult = validate.decimal(state.minTemperature, isRequired = true)
        val maxTemperatureResult = validate.decimal(state.maxTemperature, isRequired = true)
        val litersResult = validate.decimal(state.liters, isRequired = true)
        val minPhResult = validate.decimal(state.minPh)
        val maxPhResult = validate.decimal(state.maxPh)
        val minGhResult = validate.decimal(state.minGh)
        val maxGhResult = validate.decimal(state.maxGh)
        val minKhResult = validate.decimal(state.minKh)
        val maxKhResult = validate.decimal(state.maxKh)

        val hasError = listOf(
            nameResult,
            amountResult,
            minTemperatureResult,
            maxTemperatureResult,
            litersResult,
            minPhResult,
            maxPhResult,
            minGhResult,
            maxGhResult,
            minKhResult,
            maxKhResult
        ).any { it.errorCode != null }

        if (hasError) {
            state = state.copy(
                nameErrorCode = nameResult.errorCode,
                amountErrorCode = amountResult.errorCode,
                minTemperatureErrorCode = minTemperatureResult.errorCode,
                maxTemperatureErrorCode = maxTemperatureResult.errorCode,
                litersErrorCode = litersResult.errorCode,
                minPhErrorCode = minPhResult.errorCode,
                maxPhErrorCode = maxPhResult.errorCode,
                minGhErrorCode = minGhResult.errorCode,
                maxGhErrorCode = maxGhResult.errorCode,
                minKhErrorCode = minKhResult.errorCode,
                maxKhErrorCode = maxKhResult.errorCode
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
                dweller = state.dweller.copy(
                    name = state.name,
                    genus = state.genus,
                    amount = state.amount.toLong(),
                    minTemperature = convertCelsius.from(
                        state.temperatureMeasureCode,
                        state.dweller.minTemperature
                    ).result,
                    maxTemperature = convertCelsius.from(
                        state.temperatureMeasureCode,
                        state.dweller.maxTemperature
                    ).result,
                    liters = convertLiters.from(
                        state.capacityMeasureCode,
                        state.dweller.liters
                    ).result,
                    minPh = convertDKH.from(
                        state.alkalinityMeasureCode,
                        state.dweller.minPh
                    ).result,
                    maxPh = convertDKH.from(
                        state.alkalinityMeasureCode,
                        state.dweller.maxPh
                    ).result,
                    minGh = convertDKH.from(
                        state.alkalinityMeasureCode,
                        state.dweller.minGh
                    ).result,
                    maxGh = convertDKH.from(
                        state.alkalinityMeasureCode,
                        state.dweller.maxGh
                    ).result,
                    minKh = convertDKH.from(
                        state.alkalinityMeasureCode,
                        state.dweller.minKh
                    ).result,
                    maxKh = convertDKH.from(
                        state.alkalinityMeasureCode,
                        state.dweller.maxKh
                    ).result,
                    description = state.description
                )
            )

            insert(state.dweller)
            validationEventChannel.send(ValidationEvent.Success)
        }

    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }

}