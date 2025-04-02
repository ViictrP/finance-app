package com.viictrp.financeapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.ui.components.icon.CustomIcons
import com.viictrp.financeapp.ui.theme.FinanceAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    var bottomSheetVisible by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    val items = listOf("Início", "Adicionar", "Cartões")
    val routesMap = mapOf(
        "Início" to "home",
        "Adicionar" to "",
        "Cartões" to "credit_card"
    )
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Add, Icons.Filled.Email)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Add, Icons.Outlined.Email)

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
                                painter = CustomIcons.DuoTone.CreditCard,
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
                                painter = CustomIcons.DuoTone.ShoppingBag,
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
        modifier = Modifier.zIndex(1f)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    if (item == "Adicionar") {
                        bottomSheetVisible = true
                    } else {
                        routesMap[item]?.let { route ->
                            selectedItem = index
                            navController.navigate(route)
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
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