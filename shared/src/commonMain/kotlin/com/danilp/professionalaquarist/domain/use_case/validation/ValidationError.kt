package com.danilp.professionalaquarist.domain.use_case.validation

enum class ValidationError(val code: Int) {
    BLANK_FIELD_ERROR(1),
    DECIMAL_ERROR(2),
    INTEGER_ERROR(3),
    NEGATIVE_VALUE_ERROR(4)
}
