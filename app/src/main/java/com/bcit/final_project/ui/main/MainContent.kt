package com.bcit.final_project.ui.main

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

@Composable
fun MainContent(recipeState: RecipeState) {
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
                    recipeState = recipeState,
                )
            }
            composable("search") {
                Search(
                    navController = navController,
                    recipeState = recipeState,
                )
            }
            composable(
                route = "recipe/{recipeID}",
                arguments = listOf(navArgument("recipeID") {
                    type = NavType.LongType
                })
            ) {

            }
        }
    }
}

@Composable
fun Bookmarks(navController: NavController, recipeState: RecipeState) {

}

@Composable
fun Search(navController: NavController, recipeState: RecipeState) {

}

@Composable
fun ItemCard(navController: NavController) {

}
