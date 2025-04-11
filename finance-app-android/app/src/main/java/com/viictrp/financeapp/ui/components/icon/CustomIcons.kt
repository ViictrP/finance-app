package com.viictrp.financeapp.ui.components.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.viictrp.financeapp.R

object CustomIcons {
    object DuoTone {
        val AddCircle: Painter
            @Composable
            get() = painterResource(id = R.drawable.add_circle)

        val AirPlay: Painter
            @Composable
            get() = painterResource(id = R.drawable.airplay)

        val CreditCard: Painter
            @Composable
            get() = painterResource(id = R.drawable.credit_card)

        val ShoppingBag: Painter
            @Composable
            get() = painterResource(id = R.drawable.shopping_bag)

        val Settings: Painter
            @Composable
            get() = painterResource(id = R.drawable.settings)

        val Dashboard: Painter
            @Composable
            get() = painterResource(id = R.drawable.dashboard)

        val Calendar: Painter
            @Composable
            get() = painterResource(id = R.drawable.calendar)

        val User: Painter
            @Composable
            get() = painterResource(id = R.drawable.user)
    }
}