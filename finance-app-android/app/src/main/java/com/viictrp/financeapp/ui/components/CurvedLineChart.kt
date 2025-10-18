package com.viictrp.financeapp.ui.components

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import java.math.BigDecimal

data class ChartConfig(
    val lineThickness: Dp = 4.dp,
    val lineColor: Color? = null,
    val showPoints: Boolean = true,
    val pointRadius: Dp = 8.dp,
    val showLabels: Boolean = true,
    val showValues: Boolean = true,
    val labelsAndValuesTogether: Boolean = false  // Se true: label em cima, valor embaixo no mesmo local
)

@Composable
fun CurvedLineChart(
    data: Map<String, BigDecimal>,
    config: ChartConfig = ChartConfig(),
    modifier: Modifier = Modifier
) {
    val defaultLineColor = MaterialTheme.colorScheme.onTertiary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val lineColor = config.lineColor ?: defaultLineColor
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
    
    // Para preview funcionar
    val progressValue = if (animationProgress.value == 0f && data.isNotEmpty()) 1f else animationProgress.value
    
    Canvas(
        modifier = modifier.fillMaxWidth()
    ) {
        if (data.isEmpty()) return@Canvas
        
        val maxValue = data.values.maxOfOrNull { it.toFloat() } ?: 0f
        if (maxValue == 0f) return@Canvas
        
        val spacing = 8.dp.toPx()
        val totalSpacing = spacing * (data.size - 1)
        val barWidth = (size.width - totalSpacing) / data.size
        val chartHeight = if (config.showLabels) size.height * 0.85f else size.height
        val labelHeight = size.height * 0.15f
        
        val points = data.entries.mapIndexed { index, (_, value) ->
            val x = (index * (barWidth + spacing)) + barWidth / 2
            val y = chartHeight - (value.toFloat() / maxValue) * chartHeight * progressValue
            Offset(x, y)
        }
        
        // Desenhar linha curva
        if (points.size > 1) {
            val path = Path().apply {
                moveTo(points[0].x, points[0].y)
                
                for (i in 1 until points.size) {
                    val prev = points[i - 1]
                    val curr = points[i]
                    
                    // Pontos de controle para curva suave
                    val controlX1 = prev.x + (curr.x - prev.x) * 0.5f
                    val controlY1 = prev.y
                    val controlX2 = curr.x - (curr.x - prev.x) * 0.5f
                    val controlY2 = curr.y
                    
                    cubicTo(controlX1, controlY1, controlX2, controlY2, curr.x, curr.y)
                }
            }
            
            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = config.lineThickness.toPx())
            )
        }
        
        // Desenhar pontos
        if (config.showPoints) {
            points.forEach { point ->
                drawCircle(
                    color = lineColor,
                    radius = config.pointRadius.toPx(),
                    center = point
                )
            }
        }
        
        if (config.showLabels || config.showValues) {
            data.entries.forEachIndexed { index, (label, value) ->
                val x = (index * (barWidth + spacing)) + barWidth / 2
                val y = chartHeight - (value.toFloat() / maxValue) * chartHeight * progressValue
                
                if (config.labelsAndValuesTogether) {
                    // Labels e valores juntos no topo
                    if (config.showLabels) {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply {
                                color = secondaryColor.toArgb()
                                textSize = 10.dp.toPx()
                                textAlign = Paint.Align.CENTER
                            }
                            val abbreviatedLabel = if (label.length > 6) label.take(6) else label
                            canvas.nativeCanvas.drawText(
                                abbreviatedLabel,
                                x,
                                y - 24.dp.toPx(),
                                paint
                            )
                        }
                    }
                    
                    if (config.showValues) {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply {
                                color = secondaryColor.toArgb()
                                textSize = 10.dp.toPx()
                                textAlign = Paint.Align.CENTER
                            }
                            val formattedValue = "R$ %.0f".format(value.toFloat())
                            canvas.nativeCanvas.drawText(
                                formattedValue,
                                x,
                                y - 12.dp.toPx(),
                                paint
                            )
                        }
                    }
                } else {
                    // Labels e valores separados (comportamento original)
                    if (config.showLabels) {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply {
                                color = secondaryColor.toArgb()
                                textSize = 12.dp.toPx()
                                textAlign = Paint.Align.CENTER
                            }
                            val abbreviatedLabel = if (label.length > 6) label.take(6) else label
                            canvas.nativeCanvas.drawText(
                                abbreviatedLabel,
                                x,
                                chartHeight + labelHeight / 2,
                                paint
                            )
                        }
                    }
                    
                    if (config.showValues) {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply {
                                color = secondaryColor.toArgb()
                                textSize = 12.dp.toPx()
                                textAlign = Paint.Align.CENTER
                            }
                            val formattedValue = "R$ %.0f".format(value.toFloat())
                            canvas.nativeCanvas.drawText(
                                formattedValue,
                                x,
                                y - 16.dp.toPx(),
                                paint
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurvedLineChartPreview() {
    FinanceAppTheme {
        val sampleData = mapOf(
            "Seg" to BigDecimal("450.00"),
            "Ter" to BigDecimal("320.50"),
            "Qua" to BigDecimal("180.75"),
            "Qui" to BigDecimal("275.30"),
            "Sex" to BigDecimal("395.20"),
            "Sab" to BigDecimal("150.80"),
            "Dom" to BigDecimal("220.40")
        )
        
        CurvedLineChart(
            data = sampleData,
            config = ChartConfig(
                lineThickness = 6.dp,
                pointRadius = 12.dp,
                showPoints = true,
                showLabels = true,
                showValues = true,
                labelsAndValuesTogether = true
            ),
            modifier = Modifier
                .padding(16.dp)
                .height(200.dp)
        )
    }
}
