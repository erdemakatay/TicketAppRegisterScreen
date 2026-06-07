package com.turkcell.ticketapp.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.turkcell.domain.auth.AuthRepository
import com.turkcell.domain.auth.UserRole
import com.turkcell.ticketapp.screen.EventDetailScreen
import com.turkcell.ticketapp.screen.HomePageScreen
import com.turkcell.ticketapp.screen.LoginScreen
import com.turkcell.ticketapp.screen.MyTicketsScreen
import com.turkcell.ticketapp.screen.RegisterScreen
import com.turkcell.ticketapp.screen.StaffScreen
import com.turkcell.ticketapp.screen.TicketDetailScreen
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun AppNavHost(
    authRepository: AuthRepository = koinInject()
) {
    val isLoggedIn by authRepository.isLoggedIn.collectAsStateWithLifecycle(initialValue = null)
    val currentUser by authRepository.currentUser.collectAsStateWithLifecycle(initialValue = null)

    when {
        isLoggedIn == null -> SplashScreen()
        isLoggedIn == false -> UnAuthedNavHost()
        else -> when (currentUser?.role) {
            null -> SplashScreen()
            UserRole.USER -> UserNavHost()
            UserRole.STAFF, UserRole.ADMIN -> StaffNavHost()
        }
    }
}

@Composable
private fun UnAuthedNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(
                onLoginSuccess = {},
                onNavigateToRegister = { navController.navigate(Register) }
            )
        }
        composable<Register> {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Login) {
                        popUpTo(Register) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun UserNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = HomePage) {
        composable<HomePage> {
            HomePageScreen(
                onNavigateToEventDetail = { eventId ->
                    navController.navigate(EventDetail(eventId))
                },
                onNavigateToMyTickets = {
                    navController.navigate(MyTickets)
                }
            )
        }
        composable<EventDetail> {
            EventDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onPurchaseSuccess = {
                    navController.navigate(MyTickets) {
                        popUpTo(HomePage)
                    }
                }
            )
        }
        composable<MyTickets> {
            MyTicketsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { ticketId ->
                    navController.navigate(TicketDetail(ticketId))
                }
            )
        }
        composable<TicketDetail> {
            TicketDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun StaffNavHost() {
    val navController = rememberNavController()
    val authRepository: AuthRepository = koinInject()
    val scope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = StaffDashboard) {
        composable<StaffDashboard> {
            StaffScreen(
                onLogout = {
                    scope.launch { authRepository.logout() }
                }
            )
        }
    }
}

@Composable
private fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}