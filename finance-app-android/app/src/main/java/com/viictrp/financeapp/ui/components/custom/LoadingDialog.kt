package com.viictrp.financeapp.ui.components.custom

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.viictrp.financeapp.ui.theme.FinanceAppTheme

@Composable
fun LoadingDialog(
    loading: Boolean,
    onDismiss: (() -> Unit)? = null
) {
    val message = if (loading) "carregando..." else "finalizado!"

    Dialog(onDismissRequest = { onDismiss?.invoke() }) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Crossfade(targetState = loading, label = "lottieFade") { isLoading ->
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.Asset(
                        if (isLoading) "loading-lottie.json" else "completed-lottie.json"
                    )
                )
                val progress by animateLottieCompositionAsState(
                    composition,
                    iterations = if (isLoading) LottieConstants.IterateForever else 1,
                    restartOnPlay = true
                )

                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                message,
                color = MaterialTheme.colorScheme.secondary,
                style = LocalTextStyle.current.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LottieLoadingDialogPreview() {
    FinanceAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            LoadingDialog(
                loading = false
            )
        }
    }
}
