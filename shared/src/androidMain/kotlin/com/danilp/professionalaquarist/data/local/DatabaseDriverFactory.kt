package com.danilp.professionalaquarist.data.local

import android.content.Context
import com.danilp.professionalaquarist.database.AquariumDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(AquariumDatabase.Schema, context, "aquarium.db")
}