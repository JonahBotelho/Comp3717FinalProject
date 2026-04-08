package com.bcit.final_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bcit.final_project.data.RecipeDataProvider
import com.bcit.final_project.ui.main.MainContent
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
        }
    }
}
