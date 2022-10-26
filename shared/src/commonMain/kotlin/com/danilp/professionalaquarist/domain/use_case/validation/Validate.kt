package com.danilp.professionalaquarist.domain.use_case.validation

class Validate {
    fun decimal(value: String, isRequired: Boolean = false): ValidationResult =
        if (isRequired && value.isBlank())
            ValidationResult(
                successful = false,
                errorCode = ValidationError.BLANK_FIELD_ERROR.code
            )
        else if (value.ifEmpty { "0" }.toDoubleOrNull() == null)
            ValidationResult(
                successful = false,
                errorCode = ValidationError.DECIMAL_ERROR.code
            )
        else
            ValidationResult(successful = true)

    fun integer(value: String, isRequired: Boolean = false): ValidationResult =
        if (isRequired && value.isBlank())
            ValidationResult(
                successful = false,
                errorCode = ValidationError.BLANK_FIELD_ERROR.code
            )
        else if (value.ifEmpty { "0" }.toIntOrNull() == null)
            ValidationResult(
                successful = false,
                errorCode = ValidationError.INTEGER_ERROR.code
            )
        else if (value.ifEmpty { "0" }.toInt() < 0)
            ValidationResult(
                successful = false,
                errorCode = ValidationError.NEGATIVE_VALUE_ERROR.code
            )
        else
            ValidationResult(successful = true)

    fun string(value: String): ValidationResult =
        if (value.isBlank())
            ValidationResult(
                successful = false,
                errorCode = ValidationError.BLANK_FIELD_ERROR.code
            )
        else
            ValidationResult(successful = true)
}