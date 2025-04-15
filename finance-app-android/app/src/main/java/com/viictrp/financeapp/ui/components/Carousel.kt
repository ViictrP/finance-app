@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.viictrp.financeapp.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.ui.components.extension.sharedCardStyle
import com.viictrp.financeapp.ui.components.icon.CustomIcons
import com.viictrp.financeapp.ui.theme.Blue
import com.viictrp.financeapp.ui.theme.Orange
import com.viictrp.financeapp.ui.theme.Purple
import com.viictrp.financeapp.ui.theme.Secondary
import com.viictrp.financeapp.ui.theme.SecondaryDark

interface CarouselItem {
    fun getKey(): String
    fun getColor(): String
    fun getTitle(): String
    fun getDescription(): String
    fun getDetail(): String
}

val colorMap = mapOf(
    "black" to Secondary,
    "orange" to Orange,
    "blue" to Blue,
    "purple" to Purple
)

@Composable
fun <T : CarouselItem> SharedTransitionScope.CardCarousel(
    items: List<T>,
    onPageChanged: (T) -> Unit,
    modifier: Modifier = Modifier,
    pageSpacing: Dp = 16.dp,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, end = 64.dp),
    pagerState: PagerState = rememberPagerState(initialPage = 0, pageCount = { items.size }),
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {
    val haptics = LocalHapticFeedback.current
    val onPageChangedState by rememberUpdatedState(onPageChanged)

    val shape = MaterialTheme.shapes.medium
    val animationDuration = 200

    LaunchedEffect(pagerState.settledPage) {
        if (items.isNotEmpty()) {
            onPageChangedState(items[pagerState.settledPage])
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        pageSpacing = pageSpacing,
        contentPadding = contentPadding,
        key = { items[it].getKey() }
    ) { page ->
        val item = items[page]

        with(sharedTransitionScope) {
            Box(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = item.getKey().toString()),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = {_,_ ->
                            tween(durationMillis = animationDuration)
                        }
                    )
                    .sharedCardStyle(
                        color = colorMap[item.getColor()] ?: Secondary,
                        shape = shape,
                        height = 180.dp
                    )
            ) {
                CarouselCardContent(item)
            }
        }
    }
}

@Composable
fun SharedTransitionScope.CarouselCardContent(
    item: CarouselItem,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = item.getTitle(),
                color = SecondaryDark,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
            )
            IconButton(onClick = {}) {
                Icon(
                    CustomIcons.Fill.Settings,
                    modifier = Modifier
                        .size(24.dp),
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
                    CustomIcons.Fill.Calendar,
                    modifier = Modifier
                        .size(24.dp),
                    contentDescription = "Select Month",
                    tint = SecondaryDark,
                )
                Text(
                    text = " ${item.getDetail()}",
                    color = SecondaryDark,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                )
            }
            Text(
                text = item.getDescription(),
                color = SecondaryDark,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}