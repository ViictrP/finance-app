@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationApi::class)

package com.viictrp.financeapp.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.viictrp.financeapp.R
import com.viictrp.financeapp.data.remote.dto.UserDTO
import com.viictrp.financeapp.ui.navigation.Screen
import com.viictrp.financeapp.ui.screens.LocalNavAnimatedVisibilityScope
import com.viictrp.financeapp.ui.screens.LocalSharedTransitionScope
import com.viictrp.financeapp.ui.theme.FinanceAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(user: UserDTO?, modifier: Modifier, navController: NavController) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalArgumentException("No Scope found")

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val backButtonRoutes = listOf(
        Screen.Balance.route,
        Screen.CreditCardForm.route,
        Screen.CreditCardForm.route
    )
    val showBackButton = currentDestination in backButtonRoutes

    with(sharedTransitionScope) {
        TopAppBar(
            modifier = modifier.fillMaxWidth(),
            title = {
                user?.fullName?.takeIf { it.isNotBlank() }?.let { fullName ->
                    Text(
                        fullName.split(" ")[0],
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            navigationIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    with(animatedVisibilityScope) {
                        AnimatedContent(
                            targetState = showBackButton,
                            transitionSpec = {
                                (fadeIn(tween(300)) + scaleIn(
                                    animationSpec = tween(300),
                                    initialScale = 0.8f
                                )) togetherWith
                                        (fadeOut(tween(200)) + scaleOut(
                                            animationSpec = tween(200),
                                            targetScale = 0.8f
                                        ))
                            },
                            label = "Back Button Animation"
                        ) { show ->
                            if (show) {
                                BackButton(
                                    upPress = { navController.navigateUp() }
                                )
                            } else {
                                Spacer(modifier = Modifier.size(0.dp))
                            }
                        }
                    }
                    Image(
                        painter = user?.let {
                            rememberAsyncImagePainter(it.pictureUrl)
                        } ?: painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Open settings */ }) {
                    Icon(
                        CustomIcons.Filled.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(
                    modifier = Modifier.padding(end = 16.dp),
                    onClick = { /* Sync or refresh */ }) {
                    Icon(
                        CustomIcons.Filled.User,
                        contentDescription = "Sync",
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
            }
        )
    }
}

@Composable
private fun SharedTransitionScope.BackButton(upPress: () -> Unit) {
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalArgumentException("No Scope found")
    with(animatedVisibilityScope) {
        IconButton(
            onClick = upPress,
            modifier = Modifier
                .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 3f)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .size(36.dp)
                .animateEnterExit(
                    enter = scaleIn(tween(300, delayMillis = 300)),
                    exit = scaleOut(tween(20))
                )
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = .8f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = "test"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    FinanceAppTheme {
        Header(
            UserDTO(
                fullName = "Victor",
                email = "a@a.com",
                pictureUrl = "",
                accessToken = ""
            ),
            Modifier,
            rememberNavController()
        )
    }
}