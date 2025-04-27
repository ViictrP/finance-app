@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.viictrp.financeapp.ui.components.animation

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.spring
import com.viictrp.financeapp.ui.components.spatialExpressiveSpring

val boundsTransform = BoundsTransform { _, _ ->
    spatialExpressiveSpring()
}

fun <T> spatialExpressiveSpring() = spring<T>(
    dampingRatio = 0.8f,
    stiffness = 380f
)

fun <T> nonSpatialExpressiveSpring() = spring<T>(
    dampingRatio = 1f,
    stiffness = 1600f
)