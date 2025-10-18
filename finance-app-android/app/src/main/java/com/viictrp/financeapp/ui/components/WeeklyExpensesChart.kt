package com.viictrp.financeapp.ui.components

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import java.math.BigDecimal

@Composable
fun WeeklyExpensesChart(
    data: Map<String, BigDecimal>,
    modifier: Modifier = Modifier
) {
    val tertiaryColor = MaterialTheme.colorScheme.onTertiary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val animationProgress = remember { Animatable(0f) }
    
    LaunchedEffect(data) {
        if (data.isNotEmpty()) {
            animationProgress.snapTo(0f)
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800)
            )
        }
    }
    
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        if (data.isEmpty()) return@Canvas
        
        val maxValue = data.values.maxOfOrNull { it.toFloat() } ?: 0f
        if (maxValue == 0f) return@Canvas
        
        val spacing = 8.dp.toPx()
        val totalSpacing = spacing * (data.size - 1)
        val barWidth = (size.width - totalSpacing) / data.size
        val chartHeight = size.height * 0.7f
        val labelHeight = size.height * 0.3f
        
        data.entries.forEachIndexed { index, (label, value) ->
            val barHeight = (value.toFloat() / maxValue) * chartHeight * animationProgress.value
            val x = (index * (barWidth + spacing))
            val y = chartHeight - barHeight
            
            // Lado esquerdo
            drawLine(
                color = tertiaryColor,
                start = Offset(x, chartHeight),
                end = Offset(x, y),
                strokeWidth = 8f
            )
            
            // Topo
            drawLine(
                color = tertiaryColor,
                start = Offset(x, y),
                end = Offset(x + barWidth, y),
                strokeWidth = 8f
            )
            
            // Lado direito
            drawLine(
                color = tertiaryColor,
                start = Offset(x + barWidth, y),
                end = Offset(x + barWidth, chartHeight),
                strokeWidth = 8f
            )
            
            // Conectar com a próxima barra
            if (index < data.size - 1) {
                val nextX = ((index + 1) * (barWidth + spacing))
                drawLine(
                    color = tertiaryColor,
                    start = Offset(x + barWidth, chartHeight),
                    end = Offset(nextX, chartHeight),
                    strokeWidth = 8f
                )
            }
            
            // Label da data
            drawIntoCanvas { canvas ->
                val paint = Paint().apply {
                    color = secondaryColor.toArgb()
                    textSize = 12.dp.toPx()
                    textAlign = Paint.Align.CENTER
                }
                val abbreviatedLabel = if (label.length > 6) label.take(6) else label
                canvas.nativeCanvas.drawText(
                    abbreviatedLabel,
                    x + barWidth / 2,
                    chartHeight + labelHeight / 2,
                    paint
                )
            }
            
            // Valor no topo
            drawIntoCanvas { canvas ->
                val paint = Paint().apply {
                    color = secondaryColor.toArgb()
                    textSize = 12.dp.toPx()
                    textAlign = Paint.Align.CENTER
                }
                val formattedValue = "R$ %.0f".format(value.toFloat())
                canvas.nativeCanvas.drawText(
                    formattedValue,
                    x + barWidth / 2,
                    y - 8.dp.toPx(),
                    paint
                )
            }
        }
    }
}
