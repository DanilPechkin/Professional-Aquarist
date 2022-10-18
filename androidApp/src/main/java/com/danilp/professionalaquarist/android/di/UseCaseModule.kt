package com.danilp.professionalaquarist.android.di

import com.danilp.professionalaquarist.domain.use_case.calculation.aquairum.capacity.CalculateCapacity
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.ConvertDKH
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.ConvertLiters
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.ConvertMeters
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.ConvertCelsius
import com.danilp.professionalaquarist.domain.use_case.calculation.fresh_water.CalculateFreshCO2
import com.danilp.professionalaquarist.domain.use_case.validation.Validate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // ----------Validation----------
    @Provides
    @Singleton
    fun provideValidate(): Validate =
        Validate()


    // ----------Conversion----------

    //Capacity
    @Provides
    @Singleton
    fun provideConvertLiters(): ConvertLiters = ConvertLiters()

    //Temperature
    @Provides
    @Singleton
    fun provideConvertCelsius(): ConvertCelsius = ConvertCelsius()

    //Alkalinity
    @Provides
    @Singleton
    fun provideConvertDKH(): ConvertDKH = ConvertDKH()

    //Metric
    @Provides
    @Singleton
    fun provideConvertMeters(): ConvertMeters = ConvertMeters()


    // ----------Calculation----------

    //---Fresh water---

    @Provides
    @Singleton
    fun provideCalculateFreshCO2(): CalculateFreshCO2 = CalculateFreshCO2()

    // ---Aquarium---

    //Capacity

    @Provides
    @Singleton
    fun provideCalculateCapacity(): CalculateCapacity =
        CalculateCapacity()


}