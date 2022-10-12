package com.danilp.professionalaquarist.data.local

import com.danilp.professionalaquarist.database.AquariumDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(AquariumDatabase.Schema, "aquarium.db")
}