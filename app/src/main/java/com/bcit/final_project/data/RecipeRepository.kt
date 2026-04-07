package com.bcit.final_project.data

import com.google.gson.annotations.SerializedName
import io.ktor.client.call.body
import io.ktor.client.request.get

private data class MealLookupResponse(
    @SerializedName("meals")
    val meals: List<Recipe>?
)

class RecipeRepository {

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
}
