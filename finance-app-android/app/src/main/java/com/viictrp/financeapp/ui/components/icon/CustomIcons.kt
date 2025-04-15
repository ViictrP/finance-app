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

        val Burger: Painter
            @Composable
            get() = painterResource(id = R.drawable.hamburger_duotone)

        val Settings: Painter
            @Composable
            get() = painterResource(id = R.drawable.gear_six_duotone)

        val Dashboard: Painter
            @Composable
            get() = painterResource(id = R.drawable.house_duotone)

        val Calendar: Painter
            @Composable
            get() = painterResource(id = R.drawable.calendar_duotone)

        val User: Painter
            @Composable
            get() = painterResource(id = R.drawable.user_circle_duotone)
    }

    object Fill {
        val AddCircle: Painter
            @Composable
            get() = painterResource(id = R.drawable.plus_circle_fill)

        val AirPlay: Painter
            @Composable
            get() = painterResource(id = R.drawable.airplay_fill)

        val CreditCard: Painter
            @Composable
            get() = painterResource(id = R.drawable.credit_card_fill)

        val ShoppingBag: Painter
            @Composable
            get() = painterResource(id = R.drawable.shopping_bag_fill)

        val Burger: Painter
            @Composable
            get() = painterResource(id = R.drawable.hamburger_fill)

        val Settings: Painter
            @Composable
            get() = painterResource(id = R.drawable.gear_six_fill)

        val House: Painter
            @Composable
            get() = painterResource(id = R.drawable.house_fill)

        val Calendar: Painter
            @Composable
            get() = painterResource(id = R.drawable.calendar_fill)

        val User: Painter
            @Composable
            get() = painterResource(id = R.drawable.user_circle_fill)

        val Edit: Painter
            @Composable
            get() = painterResource(id = R.drawable.note_pencil_fill)

        val Menu: Painter
            @Composable
            get() = painterResource(id = R.drawable.list)

        val DotsVertical: Painter
            @Composable
            get() = painterResource(id = R.drawable.dots_three_outline_vertical_fill)
    }

    object Outline {
        val AddCircle: Painter
            @Composable
            get() = painterResource(id = R.drawable.plus_circle)

        val AirPlay: Painter
            @Composable
            get() = painterResource(id = R.drawable.airplay)

        val CreditCard: Painter
            @Composable
            get() = painterResource(id = R.drawable.credit_card)

        val ShoppingBag: Painter
            @Composable
            get() = painterResource(id = R.drawable.shopping_bag)

        val Burger: Painter
            @Composable
            get() = painterResource(id = R.drawable.hamburger)

        val Settings: Painter
            @Composable
            get() = painterResource(id = R.drawable.gear_six)

        val House: Painter
            @Composable
            get() = painterResource(id = R.drawable.house)

        val Calendar: Painter
            @Composable
            get() = painterResource(id = R.drawable.calendar)

        val User: Painter
            @Composable
            get() = painterResource(id = R.drawable.user_circle)
    }

    object OutlineBold {
        val AddCircle: Painter
            @Composable
            get() = painterResource(id = R.drawable.plus_circle_bold)

        val AirPlay: Painter
            @Composable
            get() = painterResource(id = R.drawable.airplay_bold)

        val CreditCard: Painter
            @Composable
            get() = painterResource(id = R.drawable.credit_card_bold)

        val ShoppingBag: Painter
            @Composable
            get() = painterResource(id = R.drawable.shopping_bag_bold)

        val Burger: Painter
            @Composable
            get() = painterResource(id = R.drawable.hamburger_bold)

        val Settings: Painter
            @Composable
            get() = painterResource(id = R.drawable.gear_six_bold)

        val House: Painter
            @Composable
            get() = painterResource(id = R.drawable.house_bold)

        val Calendar: Painter
            @Composable
            get() = painterResource(id = R.drawable.calendar_bold)

        val User: Painter
            @Composable
            get() = painterResource(id = R.drawable.user_circle_bold)
    }
}