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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import java.math.BigDecimal

data class BarChartConfig(
    val barStrokeWidth: Dp = 8.dp,
    val barColor: Color? = null,
    val showLabels: Boolean = true,
    val showValues: Boolean = true,
    val showConnectingLines: Boolean = true
)

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    data: Map<String, BigDecimal>,
    config: BarChartConfig = BarChartConfig()
) {
    val defaultBarColor = MaterialTheme.colorScheme.onTertiary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val barColor = config.barColor ?: defaultBarColor
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
    
    // Para preview funcionar (LaunchedEffect não roda no preview)
    val progressValue = if (animationProgress.value == 0f && data.isNotEmpty()) 1f else animationProgress.value
    
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
        val chartHeight = if (config.showLabels) size.height * 0.7f else size.height
        val labelHeight = size.height * 0.3f
        
        data.entries.forEachIndexed { index, (label, value) ->
            val barHeight = (value.toFloat() / maxValue) * chartHeight * progressValue
            val x = (index * (barWidth + spacing))
            val y = chartHeight - barHeight
            
            // Lado esquerdo
            drawLine(
                color = barColor,
                start = Offset(x, chartHeight),
                end = Offset(x, y),
                strokeWidth = config.barStrokeWidth.toPx()
            )
            
            // Topo
            drawLine(
                color = barColor,
                start = Offset(x, y),
                end = Offset(x + barWidth, y),
                strokeWidth = config.barStrokeWidth.toPx()
            )
            
            // Lado direito
            drawLine(
                color = barColor,
                start = Offset(x + barWidth, y),
                end = Offset(x + barWidth, chartHeight),
                strokeWidth = config.barStrokeWidth.toPx()
            )
            
            // Conectar com a próxima barra
            if (config.showConnectingLines && index < data.size - 1) {
                val nextX = ((index + 1) * (barWidth + spacing))
                drawLine(
                    color = barColor,
                    start = Offset(x + barWidth, chartHeight),
                    end = Offset(nextX, chartHeight),
                    strokeWidth = config.barStrokeWidth.toPx()
                )
            }
            
            // Label embaixo
            if (config.showLabels) {
                drawIntoCanvas { canvas ->
                    val paint = Paint().apply {
                        color = secondaryColor.copy(alpha = 0.7f).toArgb()
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
            }
            
            // Valor no topo
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
                        x + barWidth / 2,
                        y - 8.dp.toPx(),
                        paint
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BarChartPreview() {
    FinanceAppTheme {
        val sampleData = mapOf(
            "Restau" to BigDecimal("450.00"),
            "Transp" to BigDecimal("320.50"),
            "Casa" to BigDecimal("180.75"),
            "Shop" to BigDecimal("275.30"),
            "Outro" to BigDecimal("95.20")
        )
        
        BarChart(
            data = sampleData,
            config = BarChartConfig(
                barStrokeWidth = 6.dp,
                showConnectingLines = true,
                showLabels = true,
                showValues = true
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
