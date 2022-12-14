package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.edit

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.screens.top_bar_menu.settings.SharedPrefs
import com.danilp.professionalaquarist.domain.aquarium.AquariumDataSource
import com.danilp.professionalaquarist.domain.dweller.Dweller
import com.danilp.professionalaquarist.domain.dweller.DwellerDataSource
import com.danilp.professionalaquarist.domain.dweller.tags.DwellerTags
import com.danilp.professionalaquarist.domain.dweller.use_case.ConvertDwellerMeasures
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
class DwellerEditViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val validate: Validate,
    private val convertDwellerMeasures: ConvertDwellerMeasures,
    private val dwellerDataSource: DwellerDataSource,
    private val aquariumDataSource: AquariumDataSource
) : ViewModel() {

    var state by mutableStateOf(DwellerEditState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<Long>("dwellerId") ?: return@launch

            state = state.copy(
                dweller = if (id == -1L) Dweller.createEmpty() else
                    dwellerDataSource.getDwellerById(id) ?: Dweller.createEmpty()
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

            state = state.copy(
                name = state.dweller.name ?: "",
                genus = state.dweller.genus ?: "",
                amount = (state.dweller.amount ?: "").toString(),
                minTemperature = (state.dweller.minTemperature ?: "").toString(),
                maxTemperature = (state.dweller.maxTemperature ?: "").toString(),
                liters = (state.dweller.liters ?: "").toString(),
                minPh = (state.dweller.minPh ?: "").toString(),
                maxPh = (state.dweller.maxPh ?: "").toString(),
                minGh = (state.dweller.minGh ?: "").toString(),
                maxGh = (state.dweller.maxGh ?: "").toString(),
                minKh = (state.dweller.minKh ?: "").toString(),
                maxKh = (state.dweller.maxKh ?: "").toString(),
                description = state.dweller.description ?: "",
                typeTag = state.dweller.tags?.find { tag ->
                    listOf(
                        DwellerTags.FISH.code,
                        DwellerTags.SHRIMP.code,
                        DwellerTags.CRAYFISH.code,
                        DwellerTags.CRAB.code,
                        DwellerTags.SNAIL.code,
                        DwellerTags.BIVALVE.code
                    ).contains(tag)
                } ?: "",
                tags = state.dweller.tags ?: listOf()
            )
        }
    }

    fun onEvent(event: DwellerEditEvent) {
        when (event) {
            is DwellerEditEvent.InsertButtonPressed -> {
                submitData()
            }

            is DwellerEditEvent.DeleteButtonPressed -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state = state.copy(isLoading = true)
                    if (state.dweller.id != null)
                        delete(state.dweller.id!!)
                    validationEventChannel.send(ValidationEvent.Success)
                }
            }

            is DwellerEditEvent.NameChanged -> {
                state = state.copy(name = event.name, nameErrorCode = null)
            }

            is DwellerEditEvent.GenusChanged -> {
                state = state.copy(genus = event.genus)
            }

            is DwellerEditEvent.AmountChanged -> {
                state = state.copy(amount = event.amount, amountErrorCode = null)
            }

            is DwellerEditEvent.MinTemperatureChanged -> {
                state = state.copy(minTemperature = event.temp, minTemperatureErrorCode = null)
            }

            is DwellerEditEvent.MaxTemperatureChanged -> {
                state = state.copy(maxTemperature = event.temp, maxTemperatureErrorCode = null)
            }

            is DwellerEditEvent.LitersChanged -> {
                state = state.copy(liters = event.liters, litersErrorCode = null)
            }

            is DwellerEditEvent.MinPhChanged -> {
                state = state.copy(minPh = event.ph, minPhErrorCode = null)
            }

            is DwellerEditEvent.MaxPhChanged -> {
                state = state.copy(maxPh = event.ph, maxPhErrorCode = null)
            }

            is DwellerEditEvent.MinGhChanged -> {
                state = state.copy(minGh = event.gh, minGhErrorCode = null)
            }

            is DwellerEditEvent.MaxGhChanged -> {
                state = state.copy(maxGh = event.gh, maxGhErrorCode = null)
            }

            is DwellerEditEvent.MinKhChanged -> {
                state = state.copy(minKh = event.kh, minKhErrorCode = null)
            }

            is DwellerEditEvent.MaxKhChanged -> {
                state = state.copy(maxKh = event.kh, maxKhErrorCode = null)
            }

            is DwellerEditEvent.DescriptionChanged -> {
                state = state.copy(description = event.description)
            }

            is DwellerEditEvent.ImagePicked -> {
                state = state.copy(
                    dweller = state.dweller.copy(imageUrl = event.imageUrl)
                )
            }

            is DwellerEditEvent.TagSelected -> {
                state = state.copy(
                    tags = if (state.tags.contains(event.tag)) {
                        state.tags.filter { it != event.tag }
                    } else {
                        state.tags + event.tag
                    }
                )
            }

            is DwellerEditEvent.TypeTagSelected -> {
                state = state.copy(typeTag = event.typeTag, typeTagErrorCode = null)
            }
        }
    }

    private fun insert(dweller: Dweller) = viewModelScope.launch {
        dwellerDataSource.insertDweller(dweller)
        refreshAquarium(state.dweller.aquariumId)
    }

    private fun delete(dwellerId: Long) = viewModelScope.launch {
        dwellerDataSource.deleteDwellerById(dwellerId)
        refreshAquarium(state.dweller.aquariumId)
    }

    private fun refreshAquarium(aquariumId: Long) = viewModelScope.launch {
        aquariumDataSource.refreshAquariumById(aquariumId)
    }

    private fun submitData() {
        viewModelScope.launch(Dispatchers.IO) {

            state = state.copy(isLoading = true)

            val nameResult = validate.string(state.name)
            val amountResult = validate.integer(state.amount)
            val minTemperatureResult = validate.decimal(state.minTemperature)
            val maxTemperatureResult = validate.decimal(state.maxTemperature)
            val litersResult = validate.decimal(state.liters)
            val minPhResult = validate.decimal(state.minPh)
            val maxPhResult = validate.decimal(state.maxPh)
            val minGhResult = validate.decimal(state.minGh)
            val maxGhResult = validate.decimal(state.maxGh)
            val minKhResult = validate.decimal(state.minKh)
            val maxKhResult = validate.decimal(state.maxKh)
            val typeTagResult = validate.string(state.typeTag)

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
                maxKhResult,
                typeTagResult
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
                    maxKhErrorCode = maxKhResult.errorCode,
                    typeTagErrorCode = typeTagResult.errorCode
                )
                return@launch
            }

            state = state.copy(
                dweller = state.dweller.copy(
                    name = state.name,
                    genus = state.genus.ifBlank { null },
                    amount = state.amount.ifBlank { null }?.toLong(),
                    minTemperature = state.minTemperature.ifBlank { null }?.toDouble(),
                    maxTemperature = state.maxTemperature.ifBlank { null }?.toDouble(),
                    liters = state.liters.ifBlank { null }?.toDouble(),
                    minPh = state.minPh.ifBlank { null }?.toDouble(),
                    maxPh = state.maxPh.ifBlank { null }?.toDouble(),
                    minGh = state.minGh.ifBlank { null }?.toDouble(),
                    maxGh = state.maxGh.ifBlank { null }?.toDouble(),
                    minKh = state.minKh.ifBlank { null }?.toDouble(),
                    maxKh = state.maxKh.ifBlank { null }?.toDouble(),
                    description = state.description.ifBlank { null },
                    tags = (
                        state.tags.filter { tag ->
                            !listOf(
                                DwellerTags.FISH.code,
                                DwellerTags.BIVALVE.code,
                                DwellerTags.CRAYFISH.code,
                                DwellerTags.SHRIMP.code,
                                DwellerTags.CRAB.code,
                                DwellerTags.SNAIL.code
                            ).contains(tag)
                        } + listOf(state.typeTag)
                        ).ifEmpty { null },
                )
            )

            state = state.copy(
                dweller = convertDwellerMeasures.from(
                    state.dweller,
                    state.alkalinityMeasureCode,
                    state.capacityMeasureCode,
                    state.temperatureMeasureCode
                )
            )

            insert(state.dweller)
            validationEventChannel.send(ValidationEvent.Success)
            state = state.copy(isLoading = false)
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}
