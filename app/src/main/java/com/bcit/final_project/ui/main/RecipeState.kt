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

class RecipeState(
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    var searchQuery by mutableStateOf("")
        private set

    var searchResults by mutableStateOf<List<Recipe>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    val favourites: Flow<List<Recipe>> = recipeRepository.observeFavourites()

    fun submitSearch(name: String) {
        val trimmedName = name.trim()
        searchQuery = trimmedName
        errorMessage = null

        if (trimmedName.isBlank()) {
            searchResults = emptyList()
            isLoading = false
            return
        }

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
        searchQuery = ""
        searchResults = emptyList()
        errorMessage = null
        isLoading = false
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
