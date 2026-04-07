package com.bcit.final_project.data

import com.google.gson.annotations.SerializedName
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private data class MealLookupResponse(
    @SerializedName("meals")
    val meals: List<Recipe>?
)

class RecipeRepository(
    private val favouriteRecipeDao: FavouriteRecipeDao
) {

    suspend fun getRecipesByName(name: String): Recipes {
        val response = client.get("${SEARCH_BY_NAME}${name}")
        val result = response.body<Recipes>()
        return Recipes(result.recipes.orEmpty())
    }

    suspend fun getRecipeById(id: String): Recipe {
        val response = client.get("${SEARCH_BY_ID}${id}")
        val result = response.body<MealLookupResponse>()
        return requireNotNull(result.meals?.firstOrNull()) {
            "Invalid id ($id)"
        }
    }

    suspend fun getRandomRecipes(numberOfRecipes: Int): Recipes {
        val recipes = mutableListOf<Recipe>()

        repeat(numberOfRecipes) {
            val response = client.get(RANDOM_RECIPE)
            val result: Recipes = response.body()
            recipes += result.recipes.orEmpty()
        }

        return Recipes(recipes)
    }

    fun observeFavourites(): Flow<List<Recipe>> =
        favouriteRecipeDao.getAllFavourites().map { favourites ->
            favourites.map(FavouriteRecipe::toRecipe)
        }

    fun observeIsFavourite(recipeId: String): Flow<Boolean> =
        favouriteRecipeDao.isFavourite(recipeId)

    suspend fun saveFavourite(recipe: Recipe) {
        favouriteRecipeDao.upsertFavourite(recipe.toFavouriteEntity())
    }

    suspend fun removeFavourite(recipeId: String) {
        favouriteRecipeDao.deleteFavouriteById(recipeId)
    }

    suspend fun toggleFavourite(recipe: Recipe) {
        val recipeId = requireNotNull(recipe.id?.takeIf { it.isNotBlank() }) {
            "Favourite recipes must have a non-blank id"
        }

        if (favouriteRecipeDao.getFavouriteById(recipeId) == null) {
            favouriteRecipeDao.upsertFavourite(recipe.toFavouriteEntity())
        } else {
            favouriteRecipeDao.deleteFavouriteById(recipeId)
        }
    }
}
