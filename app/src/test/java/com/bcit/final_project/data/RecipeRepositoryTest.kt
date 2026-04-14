package com.bcit.final_project.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RecipeRepositoryTest {
    @Test
    fun toggleFavouriteAddsRecipeToObservedFavourites() = runBlocking {
        val dao = FakeFavouriteRecipeDao()
        val repository = RecipeRepository(testClient(), dao)
        val recipe = sampleRecipe(id = "100")

        repository.toggleFavourite(recipe)
        val savedRecipe = repository.getAllFavourites().first().single()
        assertEquals(recipe.copy(savedAtTime = savedRecipe.savedAtTime), savedRecipe)
        assertFalse(savedRecipe.savedAtTime == 0L)
    }

    @Test
    fun toggleFavouriteAddsThenRemovesRecipe() = runBlocking {
        val dao = FakeFavouriteRecipeDao()
        val repository = RecipeRepository(testClient(), dao)
        val recipe = sampleRecipe(id = "101")

        repository.toggleFavourite(recipe)
        assertEquals(listOf("101"), repository.getAllFavourites().first().map { it.id })

        repository.toggleFavourite(recipe)
        assertTrue(repository.getAllFavourites().first().isEmpty())
    }

    @Test
    fun searchRecipesByNameReturnsRecipeList() = runBlocking {
        val repository = RecipeRepository(
            testClient(
                content = """
                    {
                      "meals": [
                        {
                          "idMeal": "52772",
                          "strMeal": "Teriyaki Chicken Casserole",
                          "strCategory": "Chicken",
                          "strTags": "Meat,Casserole",
                          "strMealThumb": "https://example.com/image.jpg",
                          "strYoutube": "https://example.com/video",
                          "strIngredient1": "Chicken",
                          "strIngredient2": null,
                          "strIngredient3": null,
                          "strIngredient4": null,
                          "strIngredient5": null,
                          "strIngredient6": null,
                          "strIngredient7": null,
                          "strIngredient8": null,
                          "strIngredient9": null,
                          "strIngredient10": null,
                          "strIngredient11": null,
                          "strIngredient12": null,
                          "strIngredient13": null,
                          "strIngredient14": null,
                          "strIngredient15": null,
                          "strIngredient16": null,
                          "strIngredient17": null,
                          "strIngredient18": null,
                          "strIngredient19": null,
                          "strIngredient20": null,
                          "strMeasure1": "1 lb",
                          "strMeasure2": null,
                          "strMeasure3": null,
                          "strMeasure4": null,
                          "strMeasure5": null,
                          "strMeasure6": null,
                          "strMeasure7": null,
                          "strMeasure8": null,
                          "strMeasure9": null,
                          "strMeasure10": null,
                          "strMeasure11": null,
                          "strMeasure12": null,
                          "strMeasure13": null,
                          "strMeasure14": null,
                          "strMeasure15": null,
                          "strMeasure16": null,
                          "strMeasure17": null,
                          "strMeasure18": null,
                          "strMeasure19": null,
                          "strMeasure20": null
                        }
                      ]
                    }
                """
            ),
            FakeFavouriteRecipeDao()
        )

        val recipes = repository.searchRecipesByName("chicken")

        assertEquals(1, recipes.size)
        assertEquals("52772", recipes.single().id)
        assertEquals("Teriyaki Chicken Casserole", recipes.single().name)
    }

    @Test(expected = IllegalArgumentException::class)
    fun toggleFavouriteRejectsBlankId() = runBlocking {
        val repository = RecipeRepository(testClient(), FakeFavouriteRecipeDao())

        repository.toggleFavourite(sampleRecipe(id = " "))
    }

    private class FakeFavouriteRecipeDao : FavouriteRecipeDao {
        private val favourites = MutableStateFlow<List<Recipe>>(emptyList())

        override fun getAllFavourites(): Flow<List<Recipe>> = favourites

        override suspend fun getFavouriteById(recipeId: String): Recipe? =
            favourites.value.firstOrNull { it.id == recipeId }

        override suspend fun upsertFavourite(entity: Recipe) {
            val remaining = favourites.value.filterNot { it.id == entity.id }
            favourites.value = (remaining + entity).sortedByDescending { it.savedAtTime }
        }

        override suspend fun deleteFavouriteById(recipeId: String) {
            favourites.value = favourites.value.filterNot { it.id == recipeId }
        }
    }

    private fun sampleRecipe(id: String): Recipe =
        Recipe(
            id = id,
            name = "Sample Meal",
            category = "Dinner",
            tags = "Simple,Favourite",
            instructions = "Cook and serve.",
            image = "https://example.com/meal.jpg",
            video = "https://example.com/video",
            strIngredient1 = "Rice",
            strIngredient2 = "Salt",
            strIngredient3 = null,
            strIngredient4 = null,
            strIngredient5 = null,
            strIngredient6 = null,
            strIngredient7 = null,
            strIngredient8 = null,
            strIngredient9 = null,
            strIngredient10 = null,
            strIngredient11 = null,
            strIngredient12 = null,
            strIngredient13 = null,
            strIngredient14 = null,
            strIngredient15 = null,
            strIngredient16 = null,
            strIngredient17 = null,
            strIngredient18 = null,
            strIngredient19 = null,
            strIngredient20 = null,
            strMeasure1 = "1 cup",
            strMeasure2 = "1 tsp",
            strMeasure3 = null,
            strMeasure4 = null,
            strMeasure5 = null,
            strMeasure6 = null,
            strMeasure7 = null,
            strMeasure8 = null,
            strMeasure9 = null,
            strMeasure10 = null,
            strMeasure11 = null,
            strMeasure12 = null,
            strMeasure13 = null,
            strMeasure14 = null,
            strMeasure15 = null,
            strMeasure16 = null,
            strMeasure17 = null,
            strMeasure18 = null,
            strMeasure19 = null,
            strMeasure20 = null,
            savedAtTime = 0L
        )

    private fun testClient(
        content: String = """{"meals": []}""",
        status: HttpStatusCode = HttpStatusCode.OK
    ): HttpClient =
        HttpClient(
            MockEngine { _ ->
                respond(
                    content = content,
                    status = status,
                    headers = io.ktor.http.headersOf(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString()
                    )
                )
            }
        ) {
            install(ContentNegotiation) {
                gson()
            }
        }
}
