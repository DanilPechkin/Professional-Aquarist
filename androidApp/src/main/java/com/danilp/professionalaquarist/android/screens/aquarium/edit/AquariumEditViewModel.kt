package com.danilp.professionalaquarist.android.screens.aquarium.edit

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.domain.aquarium.Aquarium
import com.danilp.professionalaquarist.domain.aquarium.AquariumDataSource
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.ConvertLiters
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
    private val convertLiters: ConvertLiters,
    private val aquariumDataSource: AquariumDataSource
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
                context.getString(R.string.in_aquarium_info_shared_preferences_key),
                Context.MODE_PRIVATE
            )

            state = state.copy(
                capacityMeasureCode = sharedPreferences.getInt(
                    context.getString(R.string.capacity_measure_id_key),
                    CapacityMeasure.Liters.code
                )
            )

            state = state.copy(
                name = state.aquarium.name ?: "",
                liters = if (state.aquarium.liters == null) "" else
                    convertLiters.to(
                        state.capacityMeasureCode,
                        state.aquarium.liters!!
                    ).result.toString(),
                description = state.aquarium.description ?: ""
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

        val hasError = listOf(
            nameResult,
            litersResult
        ).any { it.errorCode != null }

        if (hasError) {
            state = state.copy(
                nameErrorCode = nameResult.errorCode,
                litersErrorCode = litersResult.errorCode
            )
            return
        }

        state = state.copy(
            aquarium = state.aquarium.copy(
                name = state.name,
                liters = convertLiters.from(
                    state.capacityMeasureCode,
                    state.liters.toDouble()
                ).result,
                description = state.description.ifBlank { null }
            )
        )

        viewModelScope.launch {
            insert(state.aquarium)
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}
