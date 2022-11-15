package com.danilp.professionalaquarist.domain.dweller.use_case

import com.danilp.professionalaquarist.domain.dweller.Dweller

class SearchDwellers {
    fun execute(dwellers: List<Dweller>, query: String): List<Dweller> =
        if (query.isBlank())
            dwellers
        else
            dwellers.filter {
                it.name!!.trim().lowercase().contains(query.lowercase())
            }
}