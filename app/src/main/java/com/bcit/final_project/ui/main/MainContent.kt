package com.bcit.final_project.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bcit.final_project.data.Recipe

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
    val favourites by recipeState.favourites.collectAsState(initial = emptyList())

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (favourites.isEmpty()) {
            Text(
                text = "No favourites saved yet.",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favourites, key = { recipe -> recipe.id }) { recipe ->
                    ItemCard(
                        recipe = recipe,
                        isFavourite = true,
                        onToggleFavourite = {
                            recipeState.toggleFavourite(recipe)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Search(navController: NavController, recipeState: RecipeState) {
    val favourites by recipeState.favourites.collectAsState(initial = emptyList())
    val favouriteIds = favourites.map { recipe -> recipe.id }.toSet()
    val visibleRecipes = if (recipeState.activeSearchQuery == null) {
        recipeState.randomRecipes
    } else {
        recipeState.searchResults
    }

    LaunchedEffect(Unit) {
        if (recipeState.randomRecipes.isEmpty()) {
            recipeState.loadRandomRecipes()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = recipeState.searchInput,
                onValueChange = recipeState::updateSearchInput,
                modifier = Modifier.weight(1f),
                singleLine = true,
                label = {
                    Text("Search recipes")
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        recipeState.submitSearch()
                    }
                ),
                trailingIcon = {
                    if (recipeState.searchInput.isNotEmpty() || recipeState.activeSearchQuery != null) {
                        IconButton(
                            onClick = recipeState::clearSearch
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                }
            )
            IconButton(
                onClick = {
                    recipeState.submitSearch()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }

        when {
            recipeState.isLoading && visibleRecipes.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            recipeState.errorMessage != null && visibleRecipes.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    Text(
                        text = recipeState.errorMessage.orEmpty(),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            recipeState.activeSearchQuery != null && visibleRecipes.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    Text(
                        text = "No recipes found for \"${recipeState.activeSearchQuery}\".",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(visibleRecipes, key = { recipe -> recipe.id }) { recipe ->
                        ItemCard(
                            recipe = recipe,
                            isFavourite = recipe.id in favouriteIds,
                            onToggleFavourite = {
                                recipeState.toggleFavourite(recipe)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCard(recipe: Recipe, isFavourite: Boolean, onToggleFavourite: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = recipe.name ?: "Untitled",
                    style = MaterialTheme.typography.titleMedium
                )
                if (!recipe.category.isNullOrBlank()) {
                    Text(
                        text = recipe.category,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            IconButton(
                onClick = onToggleFavourite
            ) {
                Icon(
                    imageVector = if (isFavourite) {
                        Icons.Default.Star
                    } else {
                        Icons.Outlined.StarBorder
                    },
                    contentDescription = if (isFavourite) {
                        "Remove favourite"
                    } else {
                        "Add favourite"
                    },
                    tint = if (isFavourite) {
                        Color(0xFFFFC107)
                    } else {
                        Color.Gray
                    }
                )
            }
        }
    }
}

@Composable
fun RecipeDescription() {

}
