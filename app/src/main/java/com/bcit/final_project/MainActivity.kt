package com.bcit.final_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bcit.final_project.data.RecipeDataProvider
import com.bcit.final_project.ui.main.MainContent
import com.bcit.final_project.ui.main.NavBar
import com.bcit.final_project.ui.main.RecipeState

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val recipeRepository = RecipeDataProvider.recipeRepository(applicationContext)
        setContent {
            val recipeState: RecipeState = viewModel(
                factory = RecipeState.factory(recipeRepository)
            )
            MainContent(recipeState = recipeState)
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
    }
}

@Composable
fun Bookmarks(navController: NavController) {

}

@Composable
fun Search(navController: NavController) {

}

@Composable
fun ItemCard(navController: NavController) {

}
