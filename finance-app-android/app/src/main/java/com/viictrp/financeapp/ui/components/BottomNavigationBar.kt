package com.viictrp.financeapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    var bottomSheetVisible by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    if (bottomSheetVisible) {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val padding = 30.dp

        val sheetMaxWidth = screenWidth - padding
        ModalBottomSheet(
            onDismissRequest = { bottomSheetVisible = false },
            sheetState = bottomSheetState,
            sheetMaxWidth = sheetMaxWidth,
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
                                Icons.Outlined.Email,
                                contentDescription = "Settings",
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.size(24.dp))
                            Text(
                                "Novo Cartão",
                                style = LocalTextStyle.current.copy(fontSize = 24.sp),
                                modifier = Modifier.clickable {
                                    navController.navigate("credit_card_form")
                                    bottomSheetVisible = false
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(24.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.AccountCircle,
                                contentDescription = "Settings",
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.size(24.dp))
                            Text(
                                "Nova Transação",
                                style = LocalTextStyle.current.copy(fontSize = 24.sp),
                                modifier = Modifier.clickable {}
                            )
                        }
                    }
                }
            }
        )
    }

    BottomAppBar(
        containerColor = Color.White,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate("home") }) {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(36.dp),
                    tint = if (currentRoute == "home") Color(0xFF2196F3) else Color.Gray
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            FloatingActionButton(
                onClick = {
                    bottomSheetVisible = true
                },
                containerColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(0.8.dp),
                modifier = Modifier.size(70.dp),
                shape = CircleShape
            ) {
                Icon(
                    Icons.Filled.Add,
                    modifier = Modifier.size(50.dp),
                    contentDescription = "Add",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { navController.navigate("credit_card") }) {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = "Cards",
                    modifier = Modifier.size(36.dp),
                    tint = if (currentRoute == "credit_card") Color(0xFF2196F3) else Color.Gray
                )
            }
        }
    }
}