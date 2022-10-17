package com.danilp.professionalaquarist.android.di

import android.app.Application
import com.danilp.professionalaquarist.data.aquarium.SqlDelightAquariumDataSource
import com.danilp.professionalaquarist.data.dweller.SqlDelightDwellerDataSource
import com.danilp.professionalaquarist.data.local.DatabaseDriverFactory
import com.danilp.professionalaquarist.data.plant.SqlDelightPlantDataSource
import com.danilp.professionalaquarist.database.AquariumDatabase
import com.danilp.professionalaquarist.domain.aquarium.AquariumDataSource
import com.danilp.professionalaquarist.domain.dweller.DwellerDataSource
import com.danilp.professionalaquarist.domain.plant.PlantDataSource
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSqlDriver(app: Application): SqlDriver =
        DatabaseDriverFactory(app).createDriver()

    @Provides
    @Singleton
    fun provideAquariumDataSource(driver: SqlDriver): AquariumDataSource =
        SqlDelightAquariumDataSource(AquariumDatabase(driver))

    @Provides
    @Singleton
    fun provideDwellerDataSource(driver: SqlDriver): DwellerDataSource =
        SqlDelightDwellerDataSource(AquariumDatabase(driver))

    @Provides
    @Singleton
    fun providePlantDataSource(driver: SqlDriver): PlantDataSource =
        SqlDelightPlantDataSource(AquariumDatabase(driver))
}