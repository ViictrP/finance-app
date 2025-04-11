package com.viictrp.financeapp.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.ui.theme.Blue
import com.viictrp.financeapp.ui.theme.Secondary
import com.viictrp.financeapp.ui.theme.Orange
import com.viictrp.financeapp.ui.theme.Purple
import kotlin.math.absoluteValue

interface CarouselItem {
    fun getKey(): String
    fun getColor(): String
    fun getTitle(): String
    fun getDescription(): String
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
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 64.dp
        )
    ) { page ->
        val card = cards[page]

        val pageOffset = pagerState.getOffsetDistanceInPages(page)

        Box(
            modifier = Modifier
                .graphicsLayer {
                    val clampedOffset = pageOffset.coerceIn(-1f, 1f)

                    alpha = when {
                        clampedOffset < 0f -> 1f + clampedOffset
                        clampedOffset > 0f -> 1f - (clampedOffset * 0.2f)
                        else -> 1f
                    }
                    scaleX = 1f - 0.10f * pageOffset.absoluteValue
                    scaleY = 1f - 0.10f * pageOffset.absoluteValue
                }
                .clip(RoundedCornerShape(24.dp))
                .background(colorMap[card.getColor()] ?: Secondary)
                .fillMaxWidth()
                .aspectRatio(1.6f) // ajuste conforme o layout
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = card.getTitle(),
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)) {
                    Icon(
                        Icons.Outlined.DateRange,
                        modifier = Modifier.size(24.dp),
                        contentDescription = "Select Month",
                        tint = Color.White,
                    )
                    Text(
                        text = "03",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp)
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
    private val description: String
) : CarouselItem {
    override fun getKey() = id
    override fun getColor() = color
    override fun getTitle() = title
    override fun getDescription() = description
}

@Composable
@Preview
fun CardCarouselPreview() {
    val items = listOf(
        DemoCarouselItem(
            id = "1",
            color = "orange",
            title = "Main Card",
            description = "This is the main card"
        ),
        DemoCarouselItem(
            id = "2",
            color = "purple",
            title = "Secondary Card",
            description = "Half-visible card"
        )
    )
    CardCarousel(
        items,
    ) { card ->
        Log.d("CardsCarousel", "page changed ${card.getTitle()}")
    }
}