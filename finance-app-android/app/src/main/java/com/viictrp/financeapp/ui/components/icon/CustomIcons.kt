package com.viictrp.financeapp.ui.components.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.viictrp.financeapp.R

object CustomIcons {
    object DuoTone {
        val AddCircle: Painter
            @Composable
            get() = painterResource(id = R.drawable.plus_circle_duotone)

        val AirPlay: Painter
            @Composable
            get() = painterResource(id = R.drawable.airplay_duotone)

        val CreditCard: Painter
            @Composable
            get() = painterResource(id = R.drawable.credit_card_duotone)

        val ShoppingBag: Painter
            @Composable
            get() = painterResource(id = R.drawable.shopping_bag_duotone)

        val Settings: Painter
            @Composable
            get() = painterResource(id = R.drawable.gear_duotone)

        val Dashboard: Painter
            @Composable
            get() = painterResource(id = R.drawable.house_duotone)

        val Calendar: Painter
            @Composable
            get() = painterResource(id = R.drawable.calendar_duotone)

        val User: Painter
            @Composable
            get() = painterResource(id = R.drawable.user_duotone)
    }
}