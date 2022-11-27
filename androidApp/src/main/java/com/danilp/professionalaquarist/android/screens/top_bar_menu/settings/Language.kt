package com.danilp.professionalaquarist.android.screens.top_bar_menu.settings

import androidx.annotation.DrawableRes
import com.danilp.professionalaquarist.android.R

enum class Language(
    val code: String,
    val language: String,
    @DrawableRes val flag: Int
) {
    English("en", "English", R.drawable.us),
    Chinese("zh", "中文", R.drawable.zh),
    German("de", "Deutsch", R.drawable.de),
    French("fr", "Français", R.drawable.fr),
    Spanish("es", "Español", R.drawable.es),
    Italian("it", "Italiano", R.drawable.it),
    Russian("ru", "Русский", R.drawable.ru),
    Japanese("ja", "日本語", R.drawable.ja),
}