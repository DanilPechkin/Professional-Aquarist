package com.danilp.professionalaquarist

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform