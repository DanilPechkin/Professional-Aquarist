package com.danilp.professionalaquarist.domain.dweller

class SearchDwellers {
    fun execute(dwellers: List<Dweller>, query: String): List<Dweller> =
        if (query.isBlank())
            dwellers
        else
            dwellers.filter {
                it.name!!.trim().lowercase().contains(query.lowercase())
            }
}