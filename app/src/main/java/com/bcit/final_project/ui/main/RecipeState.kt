package com.bcit.final_project.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bcit.final_project.data.Recipe
import com.bcit.final_project.data.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

private const val DEFAULT_RANDOM_RECIPE_COUNT = 10

class RecipeState(
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    var searchInput by mutableStateOf("")
        private set

    var activeSearchQuery by mutableStateOf<String?>(null)
        private set

    var randomRecipes by mutableStateOf<List<Recipe>>(emptyList())
        private set

    var searchResults by mutableStateOf<List<Recipe>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var selectedRecipe by mutableStateOf<Recipe?>(null)
        private set

    var isRecipeDetailLoading by mutableStateOf(false)
        private set

    val favourites: Flow<List<Recipe>> = recipeRepository.getAllFavourites()

    fun updateSearchInput(value: String) {
        searchInput = value
    }

    fun loadRandomRecipes(numberOfRecipes: Int = DEFAULT_RANDOM_RECIPE_COUNT) {
        errorMessage = null
        isLoading = true

        viewModelScope.launch {
            try {
                randomRecipes = recipeRepository
                    .getRandomRecipes(numberOfRecipes)
                    .recipes
                    .orEmpty()
            } catch (error: Exception) {
                randomRecipes = emptyList()
                errorMessage = error.message ?: "Random recipe load failed"
            } finally {
                isLoading = false
            }
        }
    }

    fun submitSearch(name: String = searchInput) {
        val trimmedName = name.trim()
        searchInput = trimmedName
        errorMessage = null

        if (trimmedName.isBlank()) {
            clearSearch()
            return
        }

        activeSearchQuery = trimmedName
        isLoading = true
        viewModelScope.launch {
            try {
                searchResults = recipeRepository.searchRecipesByName(trimmedName)
            } catch (error: Exception) {
                searchResults = emptyList()
                errorMessage = error.message ?: "Search failed"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearSearch() {
        searchInput = ""
        activeSearchQuery = null
        searchResults = emptyList()
        errorMessage = null
        isLoading = false
    }

    suspend fun getRecipeById(recipeId: String): Recipe =
        recipeRepository.getRecipeById(recipeId)

    fun loadRecipe(recipeId: String) {
        val trimmedId = recipeId.trim()
        selectedRecipe = null

        if (trimmedId.isBlank()) {
            isRecipeDetailLoading = false
            return
        }

        isRecipeDetailLoading = true
        viewModelScope.launch {
            try {
                selectedRecipe = getRecipeById(trimmedId)
            } catch (error: Exception) {
                selectedRecipe = null
            } finally {
                isRecipeDetailLoading = false
            }
        }
    }

    fun toggleFavourite(recipe: Recipe) {
        viewModelScope.launch {
            recipeRepository.toggleFavourite(recipe)
        }
    }

    companion object {
        fun factory(recipeRepository: RecipeRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    RecipeState(recipeRepository) as T
            }
    }
}
