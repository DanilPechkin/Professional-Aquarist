package com.danilp.professionalaquarist.android.navigation.nav_graphs

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@NavGraph
annotation class InAquariumNavGraph(
    val start: Boolean = false
)
