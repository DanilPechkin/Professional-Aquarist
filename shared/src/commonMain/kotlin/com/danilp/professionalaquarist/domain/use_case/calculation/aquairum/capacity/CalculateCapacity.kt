package com.danilp.professionalaquarist.domain.use_case.calculation.aquairum.capacity

import com.danilp.professionalaquarist.domain.use_case.calculation.CalculationResult
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.tan

/**
 * Takes centimeters, returns milliliters
 */
class CalculateCapacity {

    fun rectangle(length: Double, width: Double, height: Double): CalculationResult =
        CalculationResult(result = length * width * height)

    fun cylinder(height: Double, diameter: Double): CalculationResult =
        CalculationResult(result = PI * height * (diameter / 2).pow(2))

    fun halfCylinder(height: Double, diameter: Double): CalculationResult =
        CalculationResult(result = 0.5 * PI * height * (diameter / 2).pow(2))

    /**
     * @param width lateral side
     * @param fullWidth distance between flat side and rounded
     * @param length flat side
     */
    fun bowfront(
        height: Double,
        width: Double,
        fullWidth: Double,
        length: Double
    ): CalculationResult =
        if (fullWidth < width)
            CalculationResult(
                successful = false,
                errorMessageCode = CapacityError.WIDTHS_ERROR.code
            )
        else
            CalculationResult(
                result = (height * width * length) +
                        (length / 2) * (fullWidth - width) * PI * height / 2
            )

    fun cornerBowfront(height: Double, width: Double, length: Double): CalculationResult =
        CalculationResult(length * width * height * PI / 4)

    /**
     * @param fullWidth outer width
     * @param fullLength outer length
     * @param width inner width
     * @param length inner length
     */
    fun lShape(
        height: Double,
        fullWidth: Double,
        width: Double,
        fullLength: Double,
        length: Double
    ): CalculationResult =
        if (fullWidth < width)
            CalculationResult(
                successful = false,
                errorMessageCode = CapacityError.WIDTHS_ERROR.code
            )
        else if (fullLength < length)
            CalculationResult(
                successful = false,
                errorMessageCode = CapacityError.LENGTH_ERROR.code
            )
        else
            CalculationResult(result = (fullWidth * fullLength * height) - width * length * height)

    /**
     * @param fullWidth outer width
     * @param fullLength outer length
     * @param width inner width
     * @param length inner length
     */
    fun angleLShape(
        height: Double,
        fullWidth: Double,
        width: Double,
        fullLength: Double,
        length: Double,
        lengthBetweenSide: Double,
        widthBetweenSide: Double
    ): CalculationResult =
        if (fullWidth < width || lengthBetweenSide > fullWidth)
            CalculationResult(
                successful = false,
                errorMessageCode = CapacityError.WIDTHS_ERROR.code
            )
        else if (fullLength < length || widthBetweenSide > fullLength)
            CalculationResult(
                successful = false,
                errorMessageCode = CapacityError.LENGTH_ERROR.code
            )
        else
            CalculationResult(
                result = (fullWidth * fullLength * height) - (
                        ((fullLength - widthBetweenSide) * width * height) +
                                ((fullWidth - width - lengthBetweenSide) * 0.5
                                        * ((fullLength - widthBetweenSide) + length) * height)
                        )
            )

    fun ellipticalCylinder(height: Double, width: Double, length: Double): CalculationResult =
        CalculationResult(result = height * (width / 2) * (length / 2) * PI)

    /**
     * @param length the flat side
     * @param width distance between flat side and rounded
     */
    fun bullnose(height: Double, length: Double, width: Double): CalculationResult =
        CalculationResult(result = (length / 2) * width * PI * 0.5 * height)

    fun triangle(height: Double, side1: Double, side2: Double, side3: Double): CalculationResult {
        val p = side1 + side2 + side3
        val s = sqrt(p * (p - side1) * (p - side2) * (p - side3))
        return CalculationResult(result = height * s)
    }

    /**
     * @param width smaller flat side
     * @param fullWidth bigger flat side
     * @param length distance between flat sides
     */
    fun trapezoid(
        height: Double,
        width: Double,
        fullWidth: Double,
        length: Double
    ): CalculationResult =
        if (fullWidth < width)
            CalculationResult(
                successful = false,
                errorMessageCode = CapacityError.WIDTHS_ERROR.code
            )
        else
            CalculationResult(result = (0.5 * (width + fullWidth) * length) * height)

    /**
     * @param length smaller flat side
     * @param fullLength bigger flat side
     * @param width lateral side
     * @param fullWidth distance between flat sides
     */
    fun flatBackHex(
        height: Double,
        length: Double,
        fullLength: Double,
        width: Double,
        fullWidth: Double
    ): CalculationResult =
        if (fullWidth < width)
            CalculationResult(
                successful = false,
                errorMessageCode = CapacityError.WIDTHS_ERROR.code
            )
        else if (fullLength < length)
            CalculationResult(
                successful = false,
                errorMessageCode = CapacityError.LENGTH_ERROR.code
            )
        else
            CalculationResult(
                result = (height * length * width) +
                        ((fullWidth - width) * 0.5 * (fullLength + length) * height)
            )

    /**
     * @param sides number of sides
     * @param length lateral side
     */
    fun regularPolygon(height: Double, sides: Int, length: Double): CalculationResult =
        if (sides < 3)
            CalculationResult(
                successful = false,
                errorMessageCode = CapacityError.NUMBER_OF_SIDES_ERROR.code
            )
        else
            CalculationResult(
                result = height * (sides * length.pow(2) * (1 / tan(PI / sides)) / 4)
            )
}