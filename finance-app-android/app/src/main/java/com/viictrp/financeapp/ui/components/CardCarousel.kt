package com.viictrp.financeapp.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.ui.components.icon.CustomIcons
import com.viictrp.financeapp.ui.theme.Blue
import com.viictrp.financeapp.ui.theme.Orange
import com.viictrp.financeapp.ui.theme.Purple
import com.viictrp.financeapp.ui.theme.Secondary
import com.viictrp.financeapp.ui.theme.SecondaryDark
import kotlin.math.absoluteValue

interface CarouselItem {
    fun getKey(): String
    fun getColor(): String
    fun getTitle(): String
    fun getDescription(): String
    fun getDetail(): String
}

@Composable
fun <T : CarouselItem> CardCarousel(
    cards: List<T>,
    onPageChanged: (card: T) -> Unit
) {
    var pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { cards.size }
    )

    val colorMap = mapOf<String, Color>(
        "black" to Secondary,
        "orange" to Orange,
        "blue" to Blue,
        "purple" to Purple
    )

    LaunchedEffect(pagerState.currentPage) {
        if (cards.isNotEmpty()) {
            onPageChanged(cards[pagerState.currentPage])
        }
    }

    HorizontalPager(
        state = pagerState,
        pageSpacing = (-8).dp,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 64.dp
        )
    ) { page ->
        val card = cards[page]

        val pageOffset = pagerState.getOffsetDistanceInPages(page)

        Card(
            modifier = Modifier
                .graphicsLayer {
                    val clampedOffset = pageOffset.coerceIn(-1f, 1f)

                    alpha = when {
                        clampedOffset < 0f -> 1f + clampedOffset
                        clampedOffset > 0f -> 1f - (clampedOffset * 0.2f)
                        else -> 1f
                    }
                    scaleX = lerp(1f, 0.88f, pageOffset.absoluteValue)
                    scaleY = lerp(1f, 0.88f, pageOffset.absoluteValue)
                }
                .height(180.dp),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
            ),
            colors = CardDefaults.cardColors(
                containerColor = colorMap[card.getColor()] ?: Secondary
            ),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = card.getTitle(),
                        color = SecondaryDark,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp)
                    )
                    IconButton(onClick = {}) {
                        Icon(
                            CustomIcons.DuoTone.AddCircle,
                            modifier = Modifier.size(24.dp),
                            contentDescription = "Select Month",
                            tint = Color.White,
                        )
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            CustomIcons.DuoTone.Calendar,
                            modifier = Modifier.size(24.dp),
                            contentDescription = "Select Month",
                            tint = SecondaryDark,
                        )
                        Text(
                            text = " ${card.getDetail()}",
                            color = SecondaryDark,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp)
                        )
                    }
                    Text(
                        text = card.getDescription(),
                        color = SecondaryDark,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

data class DemoCarouselItem(
    private val id: String,
    private val color: String,
    private val title: String,
    private val description: String,
    private val detail: String
) : CarouselItem {
    override fun getKey() = id
    override fun getColor() = color
    override fun getTitle() = title
    override fun getDescription() = description
    override fun getDetail(): String = detail
}

@Composable
@Preview
fun CardCarouselPreview() {
    val items = listOf(
        DemoCarouselItem(
            id = "1",
            color = "orange",
            title = "Main Card",
            description = "4422",
            detail = "03"
        ),
        DemoCarouselItem(
            id = "2",
            color = "purple",
            title = "Secondary Card",
            description = "3344",
            detail = "03"
        )
    )
    CardCarousel(
        items,
    ) { card ->
        Log.d("CardsCarousel", "page changed ${card.getTitle()}")
    }
}

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + (stop - start) * fraction.coerceIn(0f, 1f)
}