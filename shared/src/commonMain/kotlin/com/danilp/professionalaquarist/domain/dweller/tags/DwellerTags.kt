package com.danilp.professionalaquarist.domain.dweller.tags

enum class DwellerTags(val code: String) {
    HERBIVOROUS("Herbivorous"), // травоядные
    CARNIVOROUS("Carnivorous"), // мясоядные

    PREDATOR("Predator"), // хищник
    PEACEFUL("Peaceful"), // миролюбивый
    TERRITORIAL("Territorial"), // территориальный

    VEIL_TAILED("Veil-tailed"), // вуалевый

    FAST_CURRENT("Fast current"), // любит быстрое течение
    MEDIUM_CURRENT("Medium current"), // любит среднее течение
    SLOW_CURRENT("Slow current"), // любит мягкое течение

    BRIGHT_LIGHT("Bright light"), // любит яркий свет
    LOW_LIGHT("Low light"), // любит слабый свет

    LARGE("Large"), // очень большой
    BIG("Big"), // большой
    MEDIUM("Medium"), // средний
    SMALL("Small"), // маленький

    MONOGAMOUS("Monogamous"), // моногамный (пара обязательна)
    POLYGAMOUS("Polygamous"), // полигамный

    LIVEBEARER("Livebearer"), // живородящие
    OVIPAROUS("Oviparous"), // яйцекладущие

    SHOAL("Shoal"), // стайный (стая обязательна)

    FISH("Fish"), // рыба
    SNAIL("Snail"), // улитка
    CRAB("Crab"), // краб
    SHRIMP("Shrimp"), // креветка
    CRAYFISH("Crayfish"), // рак
    BIVALVE("Bivalve"), // двухстворчатое

    PLANT_LOVER("Plant lover"), // любит растения
    NEEDS_SHELTER("Needs shelter"), // нуждается в укрытии

    BROADLEAF_PLANT("Broadleaf plant lover"), // любит широколистные растения
    LONG_STEMMED_PLANT("Long-stemmed plant lover"), // любит длинностебельные растения
    FLOATING_PLANT("Floating plant lover"), // любит плавающие растения
    MOSS("Moss lover"), // мох

    CLEANER("Cleaner"), // чистильщик

    PLANT_EATER("Plant eater") // угроза для растений
}