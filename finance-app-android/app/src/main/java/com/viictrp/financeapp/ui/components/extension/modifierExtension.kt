package com.viictrp.financeapp.ui.components.extension

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.viictrp.financeapp.ui.theme.Primary
import com.viictrp.financeapp.ui.theme.Secondary

fun Modifier.sharedCardStyle(
    color: Color? = Secondary,
    shape: Shape = RoundedCornerShape(16.dp),
    height: Dp? = 180.dp
): Modifier {
    return this
        .fillMaxWidth()
        .height(height ?: 180.dp)
        .border(BorderStroke(1.dp, Primary.copy(.1f)), shape)
        .clip(shape)
        .background(color ?: Secondary, shape)
}