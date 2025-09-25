@file:OptIn(
    ExperimentalSharedTransitionApi::class
)

package com.viictrp.financeapp.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.viictrp.financeapp.auth.AuthViewModel
import com.viictrp.financeapp.ui.components.BottomBarItem
import com.viictrp.financeapp.ui.components.FinanceAppBottomBar
import com.viictrp.financeapp.ui.components.FinanceAppScaffold
import com.viictrp.financeapp.ui.components.FinanceAppSnackBar
import com.viictrp.financeapp.ui.components.Header
import com.viictrp.financeapp.ui.components.extension.composableWithCompositionLocal
import com.viictrp.financeapp.ui.components.nonSpatialExpressiveSpring
import com.viictrp.financeapp.ui.components.rememberFinanceAppScaffoldState
import com.viictrp.financeapp.ui.components.spatialExpressiveSpring
import com.viictrp.financeapp.ui.navigation.Screen
import com.viictrp.financeapp.ui.navigation.SecureDestinations
import com.viictrp.financeapp.ui.navigation.rememberFinanceAppController
import com.viictrp.financeapp.ui.screens.other.LoginScreen
import com.viictrp.financeapp.ui.screens.other.SplashScreen
import com.viictrp.financeapp.ui.screens.secure.HomeScreen
import com.viictrp.financeapp.ui.screens.secure.balance.BalanceScreen
import com.viictrp.financeapp.ui.screens.secure.creditcard.CreditCardFormScreen
import com.viictrp.financeapp.ui.screens.secure.creditcard.CreditCardScreen
import com.viictrp.financeapp.ui.screens.secure.creditcard.InvoiceScreen
import com.viictrp.financeapp.ui.screens.secure.transaction.TransactionFormScreen
import com.viictrp.financeapp.ui.screens.secure.transaction.TransactionScreen
import com.viictrp.financeapp.ui.screens.secure.viewmodel.BalanceViewModel
import java.time.YearMonth

@Composable
fun MainScreen() {
    val financeAppNavController = rememberFinanceAppController()

    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            NavHost(
                navController = financeAppNavController.navController,
                startDestination = Screen.Splash.route
            ) {
                composableWithCompositionLocal(
                    route = Screen.Splash.route
                ) {
                    SplashScreen(financeAppNavController.navController)
                }

                composableWithCompositionLocal(
                    route = Screen.Login.route
                ) { from ->
                    LoginScreen(onNavigation = { destination ->
                        financeAppNavController.navigateTo(
                            destination,
                            Screen.Login.route,
                            from
                        )
                    })
                }

                composableWithCompositionLocal(
                    route = Screen.Secure.route
                ) { backStackEntry ->
                    SecureContainer(
                        backStackEntry = backStackEntry,
                        onNavigation = { id, destination, origin, from ->

                            if (id != null) {
                                when (destination) {
                                    SecureDestinations.TRANSACTION_ROUTE -> {
                                        financeAppNavController.navigateToTransaction(
                                            id,
                                            origin,
                                            from
                                        )
                                    }

                                    SecureDestinations.INVOICE_ROUTE -> {
                                        financeAppNavController.navigateToInvoice(
                                            id,
                                            origin,
                                            from
                                        )
                                    }
                                }
                            } else {
                                financeAppNavController.navigateTo(
                                    destination,
                                    origin,
                                    from
                                )
                            }
                        }
                    )
                }

                composableWithCompositionLocal(
                    route = Screen.Transaction(0, "").route,
                    arguments = listOf(
                        navArgument(SecureDestinations.TRANSACTION_KEY) {
                            type = NavType.LongType
                        }
                    )
                ) { backStackEntry ->
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val transactionId = arguments.getLong(SecureDestinations.TRANSACTION_KEY)
                    val origin = arguments.getString(SecureDestinations.ORIGIN)
                    
                    // Usa o viewModel do escopo secure compartilhado
                    val parentEntry = remember(backStackEntry) {
                        financeAppNavController.navController.getBackStackEntry(Screen.Secure.route)
                    }
                    val viewModel = hiltViewModel<BalanceViewModel>(parentEntry)

                    TransactionScreen(
                        viewModel,
                        transactionId,
                        origin!!,
                    ) {
                        financeAppNavController.pressUp()
                    }
                }

                composableWithCompositionLocal(
                    route = Screen.Invoice(0, "").route,
                    arguments = listOf(
                        navArgument(SecureDestinations.CREDIT_CARD_KEY) {
                            type = NavType.LongType
                        }
                    )
                ) { backStackEntry ->
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val creditCardId = arguments.getLong(SecureDestinations.CREDIT_CARD_KEY)
                    
                    // Usa o viewModel do escopo secure compartilhado
                    val parentEntry = remember(backStackEntry) {
                        financeAppNavController.navController.getBackStackEntry(Screen.Secure.route)
                    }
                    val viewModel = hiltViewModel<BalanceViewModel>(parentEntry)

                    InvoiceScreen(
                        viewModel,
                        creditCardId,
                        onPressUp = { financeAppNavController.pressUp() },
                        onNavigation = { id, destination ->
                            financeAppNavController.navigateToTransaction(
                                id,
                                SecureDestinations.INVOICE_ROUTE,
                                backStackEntry
                            )
                        }
                    )
                }
            }
        }
    }
}

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

@Composable
fun SecureContainer(
    modifier: Modifier = Modifier,
    backStackEntry: NavBackStackEntry,
    onNavigation: (Long?, String, String, NavBackStackEntry) -> Unit
) {
    val viewModel = hiltViewModel<BalanceViewModel>(backStackEntry)
    val context = LocalContext.current
    val authViewModel = hiltViewModel<AuthViewModel>(context as ComponentActivity)
    val nestedNavController = rememberFinanceAppController()

    val user by authViewModel.user.collectAsState()
    val balance by viewModel.balance.collectAsState()

    val navBackStackEntry by nestedNavController.navController.currentBackStackEntryAsState()
    val financeAppScaffoldState = rememberFinanceAppScaffoldState()
    val currentDestination = navBackStackEntry?.destination?.route
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No SharedElementScope found")

    LaunchedEffect(currentDestination) {
        if (balance == null) {
            viewModel.loadBalance(YearMonth.now(), defineCurrent = true)
        }
    }

    FinanceAppScaffold(
        modifier = modifier,
        topBar = {
            with(animatedVisibilityScope) {
                with(sharedTransitionScope) {
                    Header(
                        user,
                        navController = nestedNavController.navController,
                        modifier = Modifier
                            .renderInSharedTransitionScopeOverlay(
                                zIndexInOverlay = 1f,
                            )
                            .animateEnterExit(
                                enter = fadeIn(nonSpatialExpressiveSpring()) + slideInVertically(
                                    spatialExpressiveSpring()
                                ) {
                                    -it
                                },
                                exit = fadeOut(nonSpatialExpressiveSpring()) + slideOutVertically(
                                    spatialExpressiveSpring()
                                ) {
                                    -it
                                }
                            )
                    )
                }
            }
        },
        bottomBar = {
            with(animatedVisibilityScope) {
                with(sharedTransitionScope) {
                    FinanceAppBottomBar(
                        tabs = BottomBarItem.entries.toTypedArray(),
                        currentRoute = currentDestination ?: BottomBarItem.HOME.route!!,
                        navigateToRoute = nestedNavController::navigateToBottomBarRoute,
                        modifier = Modifier
                            .renderInSharedTransitionScopeOverlay(
                                zIndexInOverlay = 1f,
                            )
                            .animateEnterExit(
                                enter = fadeIn(nonSpatialExpressiveSpring()) + slideInVertically(
                                    spatialExpressiveSpring()
                                ) {
                                    it
                                },
                                exit = fadeOut(nonSpatialExpressiveSpring()) + slideOutVertically(
                                    spatialExpressiveSpring()
                                ) {
                                    it
                                }
                            )
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.systemBarsPadding(),
                snackbar = { snackbarData -> FinanceAppSnackBar(snackbarData) }
            )
        },
        snackBarHostState = financeAppScaffoldState.snackBarHostState,
    ) { padding ->
        NavHost(
            navController = nestedNavController.navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) { from ->
                HomeScreen(
                    viewModel = viewModel,
                    padding,
                    onNavigation = { id, destination ->
                        when (destination) {
                            Screen.Balance.route -> nestedNavController.navigateTo(
                                Screen.Balance.route,
                                Screen.Home.route,
                                from
                            )
                            else -> onNavigation(
                                id,
                                destination,
                                Screen.Home.route,
                                from
                            )
                        }
                    }
                )
            }

            composable(Screen.CreditCard.route) { from ->
                CreditCardScreen(
                    viewModel = viewModel,
                    padding,
                    onNavigation = { id, route ->
                        onNavigation(
                            id,
                            route,
                            Screen.CreditCard.route,
                            from
                        )
                    }
                )
            }

            composable(
                route = "secure/balance?origin={origin}",
                arguments = listOf(
                    navArgument("origin") {
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ) { from ->
                BalanceScreen(viewModel = viewModel, padding)
            }

            composable(Screen.TransactionForm.route) { from ->
                TransactionFormScreen(viewModel = viewModel, padding)
            }

            composable(Screen.CreditCardForm.route) { from ->
                CreditCardFormScreen(viewModel = viewModel, padding)
            }
        }
    }
}