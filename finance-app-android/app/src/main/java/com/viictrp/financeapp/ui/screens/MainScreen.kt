@file:OptIn(
    ExperimentalSharedTransitionApi::class
)

package com.viictrp.financeapp.ui.screens

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
import androidx.compose.ui.Modifier
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
import com.viictrp.financeapp.ui.navigation.PublicDestinations
import com.viictrp.financeapp.ui.navigation.SecureDestinations
import com.viictrp.financeapp.ui.navigation.rememberFinanceAppController
import com.viictrp.financeapp.ui.screens.other.LoginScreen
import com.viictrp.financeapp.ui.screens.other.SplashScreen
import com.viictrp.financeapp.ui.screens.secure.HomeScreen
import com.viictrp.financeapp.ui.screens.secure.balance.BalanceScreen
import com.viictrp.financeapp.ui.screens.secure.creditcard.CreditCardFormScreen
import com.viictrp.financeapp.ui.screens.secure.creditcard.CreditCardScreen
import com.viictrp.financeapp.ui.screens.secure.creditcard.invoice.InvoiceScreen
import com.viictrp.financeapp.ui.screens.secure.transaction.TransactionFormScreen
import com.viictrp.financeapp.ui.screens.secure.transaction.TransactionScreen
import com.viictrp.financeapp.ui.screens.viewmodel.BalanceViewModel
import java.time.YearMonth

@Composable
fun MainScreen() {
    val financeAppNavController = rememberFinanceAppController()
    val balanceViewModel = hiltViewModel<BalanceViewModel>()
    val authViewModel = hiltViewModel<AuthViewModel>()

    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            NavHost(
                navController = financeAppNavController.navController,
                startDestination = PublicDestinations.SPLASH_ROUTE
            ) {
                composableWithCompositionLocal(
                    route = PublicDestinations.SPLASH_ROUTE
                ) {
                    SplashScreen(financeAppNavController.navController, authViewModel)
                }

                composableWithCompositionLocal(
                    route = PublicDestinations.LOGIN_ROUTE
                ) {
                    LoginScreen(financeAppNavController.navController, authViewModel)
                }

                composableWithCompositionLocal(
                    route = SecureDestinations.SECURE_ROUTE
                ) {
                    SecureContainer(
                        viewModel = balanceViewModel,
                        authViewModel = authViewModel,
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
                    route = "${SecureDestinations.TRANSACTION_ROUTE}/" +
                            "{${SecureDestinations.TRANSACTION_KEY}}" +
                            "?origin={${SecureDestinations.ORIGIN}}",
                    arguments = listOf(
                        navArgument(SecureDestinations.TRANSACTION_KEY) {
                            type = NavType.LongType
                        }
                    )
                ) { backStackEntry ->
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val transactionId = arguments.getLong(SecureDestinations.TRANSACTION_KEY)
                    val origin = arguments.getString(SecureDestinations.ORIGIN)

                    TransactionScreen(
                        transactionId,
                        origin!!,
                        balanceViewModel,
                    )
                }

                composableWithCompositionLocal(
                    route = "${SecureDestinations.INVOICE_ROUTE}/" +
                            "{${SecureDestinations.CREDIT_CARD_KEY}}",
                    arguments = listOf(
                        navArgument(SecureDestinations.CREDIT_CARD_KEY) {
                            type = NavType.LongType
                        }
                    )
                ) { backStackEntry ->
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val creditCardId = arguments.getLong(SecureDestinations.CREDIT_CARD_KEY)

                    InvoiceScreen(creditCardId, balanceViewModel)
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
    viewModel: BalanceViewModel,
    authViewModel: AuthViewModel,
    onNavigation: (Long?, String, String, NavBackStackEntry) -> Unit
) {
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
        if (currentDestination == SecureDestinations.HOME_ROUTE && balance == null) {
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
            startDestination = SecureDestinations.HOME_ROUTE
        ) {
            composable(SecureDestinations.HOME_ROUTE) { from ->
                HomeScreen(
                    viewModel = viewModel,
                    padding,
                    onNavigation = { id, destination ->
                        when (destination) {
                            SecureDestinations.BALANCE_ROUTE -> nestedNavController.navigateTo(
                                SecureDestinations.BALANCE_ROUTE,
                                SecureDestinations.HOME_ROUTE,
                                from
                            )
                            else -> onNavigation(
                                id,
                                destination,
                                SecureDestinations.HOME_ROUTE,
                                from
                            )
                        }
                    }
                )
            }

            composable(SecureDestinations.CREDIT_CARD_ROUTE) { from ->
                CreditCardScreen(
                    viewModel,
                    padding,
                    onNavigation = { id, route ->
                        onNavigation(
                            id,
                            route,
                            SecureDestinations.CREDIT_CARD_ROUTE,
                            from
                        )
                    }
                )
            }

            composable(SecureDestinations.BALANCE_ROUTE) { from ->
                BalanceScreen(viewModel, padding)
            }

            composable(SecureDestinations.TRANSACTION_FORM_ROUTE) { from ->
                TransactionFormScreen(viewModel, padding)
            }

            composable(SecureDestinations.CREDIT_CARD_FORM_ROUTE) { from ->
                CreditCardFormScreen(viewModel, padding)
            }
        }
    }
}