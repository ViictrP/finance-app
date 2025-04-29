@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.viictrp.financeapp.ui.components

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.application.dto.CreditCardDTO
import com.viictrp.financeapp.ui.components.animation.boundsTransform
import com.viictrp.financeapp.ui.screens.LocalNavAnimatedVisibilityScope
import com.viictrp.financeapp.ui.screens.LocalSharedTransitionScope
import com.viictrp.financeapp.ui.theme.Blue
import com.viictrp.financeapp.ui.theme.Orange
import com.viictrp.financeapp.ui.theme.Purple
import com.viictrp.financeapp.ui.theme.Secondary
import com.viictrp.financeapp.ui.theme.SecondaryDark

data class CreditCardSharedKey(
    val creditCardId: Long,
    val type: CreditCardSharedKeyElementType
) {
}

enum class CreditCardSharedKeyElementType {
    Bounds,
    Title,
    InvoiceClosingDay,
    Number,
    Icon,
    Actions
}

val colorMap = mapOf(
    "black" to Secondary,
    "orange" to Orange,
    "blue" to Blue,
    "purple" to Purple
)

@Composable
fun CreditCardBox(
    creditCard: CreditCardDTO,
    modifier: Modifier = Modifier,
    onClick: ((creditCard: CreditCardDTO) -> Unit)? = {},
) {
    val haptics = LocalHapticFeedback.current

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No sharedTransitionScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No animatedVisibilityScope found")

    with(sharedTransitionScope) {
        val roundedCornerAnimation by animatedVisibilityScope.transition
            .animateDp(label = "rounded corner") { enterExit: EnterExitState ->
                when (enterExit) {
                    EnterExitState.PreEnter -> 0.dp
                    EnterExitState.Visible -> 16.dp
                    EnterExitState.PostExit -> 16.dp
                }
            }

        FinanceAppSurface(
            color = colorMap[creditCard.color] ?: Secondary,
            modifier = modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = CreditCardSharedKey(
                            creditCardId = creditCard.id!!,
                            type = CreditCardSharedKeyElementType.Bounds
                        )
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = boundsTransform,
                    clipInOverlayDuringTransition = OverlayClip(
                        RoundedCornerShape(
                            roundedCornerAnimation
                        )
                    ),
                    enter = fadeIn(),
                    exit = fadeOut()
                )
                .height(180.dp)
                .clickable {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick?.invoke(creditCard)
                },
            shape = RoundedCornerShape(
                roundedCornerAnimation
            ),
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = creditCard.title,
                        color = SecondaryDark,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                        modifier = Modifier
                            .sharedBounds(
                                rememberSharedContentState(
                                    key = CreditCardSharedKey(
                                        creditCardId = creditCard.id,
                                        type = CreditCardSharedKeyElementType.Title
                                    )
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = boundsTransform
                            )
                            .wrapContentWidth()
                    )
                    IconButton(onClick = {}) {
                        Icon(
                            CustomIcons.Filled.Settings,
                            modifier = Modifier
                                .sharedBounds(
                                    rememberSharedContentState(
                                        key = CreditCardSharedKey(
                                            creditCardId = creditCard.id,
                                            type = CreditCardSharedKeyElementType.Actions
                                        )
                                    ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = boundsTransform
                                )
                                .wrapContentWidth()
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
                            CustomIcons.Filled.Calendar,
                            modifier = Modifier
                                .sharedBounds(
                                    rememberSharedContentState(
                                        key = CreditCardSharedKey(
                                            creditCardId = creditCard.id,
                                            type = CreditCardSharedKeyElementType.Icon
                                        )
                                    ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = boundsTransform
                                )
                                .wrapContentWidth()
                                .size(24.dp),
                            contentDescription = "Select Month",
                            tint = SecondaryDark,
                        )
                        Text(
                            text = " ${creditCard.invoiceClosingDay}",
                            color = SecondaryDark,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                            modifier = Modifier
                                .sharedBounds(
                                    rememberSharedContentState(
                                        key = CreditCardSharedKey(
                                            creditCardId = creditCard.id,
                                            type = CreditCardSharedKeyElementType.InvoiceClosingDay
                                        )
                                    ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = boundsTransform
                                )
                                .wrapContentWidth()
                        )
                    }
                    Text(
                        text = creditCard.number,
                        color = SecondaryDark,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .sharedBounds(
                                rememberSharedContentState(
                                    key = CreditCardSharedKey(
                                        creditCardId = creditCard.id,
                                        type = CreditCardSharedKeyElementType.Number
                                    )
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = boundsTransform
                            )
                            .wrapContentWidth()
                    )
                }
        }
        }
    }
}