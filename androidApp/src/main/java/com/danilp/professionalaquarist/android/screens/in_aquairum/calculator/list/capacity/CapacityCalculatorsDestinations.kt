package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.list.capacity

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Rectangle
import androidx.compose.ui.graphics.vector.ImageVector
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.android.screens.destinations.AngleLShapeCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.BowfrontCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.BullnoseCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.CornerBowfrontCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.CylinderCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.EllipticalCylinderCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.FlatBackHexCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.HalfCylinderCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.LShapeCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.RectangleCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.RegularPolygonCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.TrapezoidCalculatorDestination
import com.danilp.professionalaquarist.android.screens.destinations.TriangleCalculatorDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class CapacityCalculatorsDestinations(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val title: Int
) {
    Rectangle(
        RectangleCalculatorDestination,
        Icons.Rounded.Rectangle,
        R.string.rectangle_title
    ),
    Cylinder(
        CylinderCalculatorDestination,
        Icons.Rounded.Circle,
        R.string.cylinder_title
    ),
    HalfCylinder(
        HalfCylinderCalculatorDestination,
        Icons.Rounded.Circle,
        R.string.halfcylinder_title
    ),
    Bowfront(
        BowfrontCalculatorDestination,
        Icons.Rounded.Circle,
        R.string.bowfront_title
    ),
    CornerBowfront(
        CornerBowfrontCalculatorDestination,
        Icons.Rounded.Circle,
        R.string.cornerbowfront_title
    ),
    LShape(
        LShapeCalculatorDestination,
        Icons.Rounded.Circle,
        R.string.lshape_title
    ),
    AngleLShape(
        AngleLShapeCalculatorDestination,
        Icons.Rounded.Circle,
        R.string.anglelshape_title
    ),
    EllipticalCylinder(
        EllipticalCylinderCalculatorDestination,
        Icons.Rounded.Circle,
        R.string.ellipticalcylinder_title
    ),
    Bullnose(
        BullnoseCalculatorDestination,
        Icons.Rounded.Circle,
        R.string.bullnose_title
    ),
    Triangle(
        TriangleCalculatorDestination,
        Icons.Rounded.Circle,
        R.string.triangle_title
    ),
    Trapezoid(
        TrapezoidCalculatorDestination,
        Icons.Rounded.Circle,
        R.string.trapezoid_title
    ),
    FlatBackHex(
        FlatBackHexCalculatorDestination,
        Icons.Rounded.Circle,
        R.string.flatbackhex_title
    ),
    RegularPolygon(
        RegularPolygonCalculatorDestination,
        Icons.Rounded.Circle,
        R.string.regularpolygon_title
    )
}
