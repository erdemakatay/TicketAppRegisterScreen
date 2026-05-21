package com.turkcell.ticketapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.turkcell.domain.AuthRepository
import com.turkcell.ticketapp.screen.HomePageScreen
import com.turkcell.ticketapp.screen.LoginScreen
import com.turkcell.ticketapp.screen.RegisterScreen
import org.koin.compose.koinInject


@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    authRepository: AuthRepository = koinInject ()
) {

    val isLoggedIn by authRepository.isLoggedIn.collectAsStateWithLifecycle(initialValue = null)

    when (isLoggedIn) {
        null -> SplashScreen()
        true -> AuthedNavhost(navController)
        false -> UnAuthedNavHost(navController)
    }
}



@Composable
private fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center){
        CircularProgressIndicator()
    }
}

@Composable
private fun AuthedNavhost (navController: NavHostController) {
    NavHost(navController = navController, startDestination = HomePage){
        composable<HomePage> {
            HomePageScreen()
        }
    }
}

@Composable
private fun UnAuthedNavHost(navController : NavHostController) {
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
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}



