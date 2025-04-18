package com.viictrp.financeapp.ui.components.custom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.ui.components.icon.CustomIcons
import com.viictrp.financeapp.ui.theme.FinanceAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavController) {
    val haptic = LocalHapticFeedback.current
    var bottomSheetVisible by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedIndex = when (currentRoute) {
        "home" -> 0
        "credit_card" -> 2
        else -> -1 // nada selecionado, como login, etc
    }

    val items = listOf("Início", "Adicionar", "Cartões")
    val routesMap = mapOf(
        "Início" to "home",
        "Adicionar" to "",
        "Cartões" to "credit_card"
    )
    val selectedIcons = listOf(
        CustomIcons.Filled.House,
        CustomIcons.DuoTone.AddCircle,
        CustomIcons.Filled.CreditCard
    )
    val unselectedIcons = listOf(
        CustomIcons.Outline.House,
        CustomIcons.DuoTone.AddCircle,
        CustomIcons.Outline.CreditCard
    )

    if (bottomSheetVisible) {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val padding = 30.dp

        val sheetMaxWidth = screenWidth - padding

        ModalBottomSheet(
            onDismissRequest = { bottomSheetVisible = false },
            sheetState = bottomSheetState,
            sheetMaxWidth = sheetMaxWidth,
            containerColor = MaterialTheme.colorScheme.primary,
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = CustomIcons.Outline.CreditCard,
                                contentDescription = "Settings",
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.size(24.dp))
                            Text(
                                "Novo Cartão",
                                style = LocalTextStyle.current.copy(fontSize = 24.sp),
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.clickable {
                                    navController.navigate("credit_card_form")
                                    bottomSheetVisible = false
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(24.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = CustomIcons.Outline.ShoppingBag,
                                contentDescription = "Settings",
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.size(24.dp))
                            Text(
                                "Nova Transação",
                                style = LocalTextStyle.current.copy(fontSize = 24.sp),
                                modifier = Modifier.clickable {},
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        )
    }

    // Navigation Bar
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.height(80.dp)
    ) {
        items.forEachIndexed { index, item ->
            val route = routesMap[item]
            val isSelected = route == currentRoute

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = if (isSelected) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item,
                        modifier = Modifier.size(if (index == 1) 48.dp else 28.dp),
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                    )
                },
                label = {

                },
                selected = selectedIndex == index,
                onClick = {
                    if (item == "Adicionar") {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        bottomSheetVisible = true
                    } else {
                        route?.let {
                            if (it != currentRoute) {
                                navController.navigate(it) {
                                    popUpTo("main_graph") { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.tertiary
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    FinanceAppTheme {
        BottomNavigationBar(rememberNavController())
    }
}