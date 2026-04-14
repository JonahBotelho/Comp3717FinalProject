package com.bcit.final_project.data

import com.google.gson.annotations.SerializedName
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow

private data class MealLookupResponse(
    @SerializedName("meals")
    val meals: List<Recipe>?
)

class RecipeRepository(
    private val client: HttpClient,
    private val favouriteRecipeDao: FavouriteRecipeDao
) {

    suspend fun searchRecipesByName(name: String): List<Recipe> {
        val response = client.get("${SEARCH_BY_NAME}${name}")
        require(response.status.isSuccess()) {
            "Recipe search failed: ${response.status}"
        }
        val result = response.body<Recipes>()
        return result.recipes.orEmpty()
    }

    suspend fun getRecipeById(id: String): Recipe {
        val response = client.get("${SEARCH_BY_ID}${id}")
        require(response.status.isSuccess()) {
            "Recipe lookup failed: ${response.status}"
        }
        val result = response.body<MealLookupResponse>()
        return requireNotNull(result.meals?.firstOrNull()) {
            "Invalid id ($id)"
        }
    }

    suspend fun getRandomRecipes(numberOfRecipes: Int): Recipes {
        val recipes = mutableListOf<Recipe>()

        repeat(numberOfRecipes) {
            val response = client.get(RANDOM_RECIPE)
            require(response.status.isSuccess()) {
                "Random recipe request failed: ${response.status}"
            }
            val result: Recipes = response.body()
            recipes += result.recipes.orEmpty()
        }

        return Recipes(recipes)
    }

    fun getAllFavourites(): Flow<List<Recipe>> =
        favouriteRecipeDao.getAllFavourites()

    suspend fun toggleFavourite(recipe: Recipe) {
        val recipeId = recipe.id.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("Favourite recipes must have a non-blank id")

        if (favouriteRecipeDao.getFavouriteById(recipeId) == null) {
            favouriteRecipeDao.upsertFavourite(recipe.setFavourite())
        } else {
            favouriteRecipeDao.deleteFavouriteById(recipeId)
        }
    }
}
