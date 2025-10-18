package com.viictrp.financeapp.ui.helper

import androidx.compose.runtime.Composable
import com.viictrp.financeapp.ui.components.CustomIcons

val map = mapOf(
    "home" to "Casa",
    "shop" to "Shop",
    "credit_card" to "Cartão",
    "other" to "Outro",
    "food" to "Restaurante",
    "transportation" to "Transporte"
)

fun categoryHelper(category: String): String {
    return map.getOrDefault(category, "Outro")
}