@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.viictrp.financeapp.ui.components

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.application.dto.TransactionDTO
import com.viictrp.financeapp.application.enums.TransactionType
import com.viictrp.financeapp.ui.components.animation.boundsTransform
import com.viictrp.financeapp.ui.helper.categoryHelper
import com.viictrp.financeapp.ui.navigation.SecureDestinations
import com.viictrp.financeapp.ui.screens.LocalNavAnimatedVisibilityScope
import com.viictrp.financeapp.ui.screens.LocalSharedTransitionScope
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


data class TransactionCardSharedElementKey(
    val transactionId: Long,
    val origin: String,
    val type: FinanceAppSharedElementType
)

enum class FinanceAppSharedElementType {
    Bounds,
    Image,
    Title,
    Tagline,
    Background
}

@Composable
fun TransactionCard(
    transaction: TransactionDTO,
    tag: String? = null,
    tagColor: Color? = null,
    origin: String,
    onClick: ((id: Long) -> Unit)? = null
) {

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
            shape = RoundedCornerShape(
                roundedCornerAnimation
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(
                            key = TransactionCardSharedElementKey(
                                transactionId = transaction.id!!,
                                origin = origin,
                                type = FinanceAppSharedElementType.Bounds
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
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        if (onClick != null) {
                            onClick(transaction.id)
                        }
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            CustomIcons.Outline.Burger,
                            modifier = Modifier.size(26.dp),
                            contentDescription = "Select Month",
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                        Spacer(modifier = Modifier.size(16.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    categoryHelper(transaction.category),
                                    style = LocalTextStyle.current.copy(fontSize = 16.sp),
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5F)
                                )
                                tag?.let {
                                    Text(
                                        it,
                                        style = LocalTextStyle.current.copy(fontSize = 14.sp),
                                        color = tagColor
                                            ?: MaterialTheme.colorScheme.secondary.copy(alpha = 0.5F)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.size(8.dp))

                            Text(
                                text = transaction.description,
                                style = LocalTextStyle.current.copy(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.secondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .sharedBounds(
                                        rememberSharedContentState(
                                            key = TransactionCardSharedElementKey(
                                                transactionId = transaction.id,
                                                origin = origin,
                                                type = FinanceAppSharedElementType.Title
                                            )
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        enter = fadeIn(nonSpatialExpressiveSpring()),
                                        exit = fadeOut(nonSpatialExpressiveSpring()),
                                        boundsTransform = boundsTransform,
                                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                                    )
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = transaction.date.format(
                                DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
                            ),
                            style = LocalTextStyle.current.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5F)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                                .format(transaction.amount),
                            style = LocalTextStyle.current.copy(fontSize = 20.sp),
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionCardPreview() {
    FinanceAppTheme {
        TransactionCard(
            transaction = TransactionDTO(
                id = 1,
                description = "Description",
                amount = BigDecimal.valueOf(100.0),
                category = "shop",
                type = TransactionType.RECURRING,
                date = LocalDateTime.now(),
                isInstallment = false,
                installmentAmount = 1,
                installmentId = null,
                creditCardId = null,
                installmentNumber = 1
            ),
            origin = SecureDestinations.HOME_ROUTE
        )
    }
}
