package com.bcit.final_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    NavBar(
                        navController = navController
                    )
                }
            ) { sPadding ->

                NavHost(
                    navController = navController,
                    startDestination = "search",
                    modifier = Modifier.padding(sPadding)
                ) {
                    composable("bookmarks") {
                        Bookmarks(
                            navController = navController,
                        )
                    }
                    composable("search") {
                        Search(
                            navController = navController,
                        )
                    }
                    composable("settings") {
                        Settings(
                            navController = navController,
                        )
                    }
                    composable(
                        route = "recipe/{recipeID}",
                        arguments = listOf(navArgument("recipeID") {
                            type = NavType.LongType
                        })
                    ) { backStackEntry ->

                    }
                }
            }
        }
    }
}

@Composable
fun Bookmarks(navController: NavController) {

}

@Composable
fun Search(navController: NavController) {

}

@Composable
fun Settings(navController: NavController) {

}

