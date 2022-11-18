package com.danilp.buildsrc

private object Versions {
    const val ktor = "2.1.3"
    const val sqlDelight = "1.5.4"
    const val destinations = "1.7.27-beta"
    const val coroutines = "1.6.4"
    const val compose = "1.4.0-alpha02"
    const val hilt = "2.44"
    const val hiltCompose = "1.0.0"
}

object Deps {

    object Ktor {
        const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val okHttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
        const val darwin = "io.ktor:ktor-client-darwin:${Versions.ktor}"
    }

    object SQLDelight {
        const val runtime = "com.squareup.sqldelight:runtime:${Versions.sqlDelight}"
        const val androidDriver = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
        const val nativeDriver = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"
    }

    object DaggerHilt {
        const val android = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
        const val composeNavigation = "androidx.hilt:hilt-navigation-compose:${Versions.hiltCompose}"
        const val composeCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltCompose}"
    }

    object Destinations {
        const val core = "io.github.raamcosta.compose-destinations:core:${Versions.destinations}"
        const val ksp_ = "io.github.raamcosta.compose-destinations:ksp:${Versions.destinations}"
    }

    object Coroutines {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    }

    object Compose {
        const val ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val uiPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
        const val material3 = "androidx.compose.material3:material3:1.1.0-alpha02"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0-alpha02"
        const val animation = "androidx.compose.animation:animation:${Versions.compose}"
        const val foundation = "androidx.compose.foundation:foundation:${Versions.compose}"
        const val foundationLayout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
        const val icons = "androidx.compose.material:material-icons-extended:${Versions.compose}"
        const val swipeRefresh = "com.google.accompanist:accompanist-swiperefresh:0.26.2-beta"
        const val uiController = "com.google.accompanist:accompanist-systemuicontroller:0.27.1"
        const val unitTest = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"
        const val debugTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
        const val debugTest = "androidx.compose.ui:ui-test-manifest:${Versions.compose}"
        const val navigation = "androidx.navigation:navigation-compose:2.5.2"
    }

}