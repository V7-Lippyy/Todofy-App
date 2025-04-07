package com.example.todofy.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todofy.ui.screens.add_edit_todo.AddEditTodoScreen
import com.example.todofy.ui.screens.home.HomeScreen
import com.example.todofy.ui.screens.splash.SplashScreen
import com.example.todofy.ui.screens.statistics.StatisticsScreen
import com.example.todofy.ui.theme.TodofyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menginstall splash screen menggunakan SplashScreen API
        installSplashScreen()

        setContent {
            TodofyTheme {
                TodofyNavigation()
            }
        }
    }
}

@Composable
fun TodofyNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAddTodo = {
                    navController.navigate(Screen.AddTodo.route)
                },
                onNavigateToEditTodo = { todoId ->
                    navController.navigate(Screen.EditTodo.createRoute(todoId))
                },
                onNavigateToStatistics = {
                    navController.navigate(Screen.Statistics.route)
                }
            )
        }

        composable(Screen.AddTodo.route) {
            AddEditTodoScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = Screen.EditTodo.route,
            arguments = listOf(
                navArgument("todoId") {
                    type = NavType.IntType
                }
            )
        ) {
            AddEditTodoScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object AddTodo : Screen("add_todo")
    object EditTodo : Screen("edit_todo/{todoId}") {
        fun createRoute(todoId: Int): String {
            return "edit_todo/$todoId"
        }
    }
    object Statistics : Screen("statistics")
}