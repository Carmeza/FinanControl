package com.example.financontrol.ui.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.financontrol.ui.compose.screens.AddTransactionScreen
import com.example.financontrol.ui.compose.screens.HistoryScreen
import com.example.financontrol.ui.compose.screens.MainScreen
import com.example.financontrol.viewmodel.TransactionViewModel

private object Routes {
    const val MAIN = "main"
    const val HISTORY = "history"
    const val ADD = "add"
    const val EDIT = "edit"
}

@Composable
fun FinanNav(vm: TransactionViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MAIN
    ) {
        composable(Routes.MAIN) {
            MainScreen(
                vm = vm,
                onGoHistory = { navController.navigate(Routes.HISTORY) },
                onGoAdd = { navController.navigate(Routes.ADD) }
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreen(
                vm = vm,
                onBack = { navController.popBackStack() },
                onEdit = { id -> navController.navigate("${Routes.EDIT}/$id") }
            )
        }

        composable(Routes.ADD) {
            AddTransactionScreen(
                vm = vm,
                onBack = { navController.popBackStack() },
                editId = -1 // crear
            )
        }

        composable(
            route = "${Routes.EDIT}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1

            AddTransactionScreen(
                vm = vm,
                onBack = { navController.popBackStack() },
                editId = id
            )
        }
    }
}
