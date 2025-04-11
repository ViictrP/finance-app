package com.viictrp.financeapp.ui.helper

val map = mapOf(
    "home" to "Casa",
    "shop" to "Shop",
    "credit_card" to "Cartão",
    "other" to "Outro",
    "food" to "Restaurante"
)

fun categoryHelper(category: String): String {
    return map.getOrDefault(category, "Outro")
}