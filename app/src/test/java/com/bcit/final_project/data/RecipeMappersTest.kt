package com.bcit.final_project.data

import org.junit.Assert.assertEquals
import org.junit.Test

class RecipeMappersTest {
    @Test
    fun recipeRoundTripPreservesStoredFields() {
        val recipe = sampleRecipe(id = "52772", tags = "Dinner,Quick")

        val restored = recipe.toFavouriteEntity(savedAtEpochMillis = 1234L).toRecipe()

        assertEquals(recipe, restored)
        assertEquals(listOf("Dinner", "Quick"), restored.tagsList)
        assertEquals("1 cup", restored.ingredientsList["Rice"])
    }

    @Test(expected = IllegalArgumentException::class)
    fun toFavouriteEntityRejectsBlankId() {
        sampleRecipe(id = " ").toFavouriteEntity()
    }

    private fun sampleRecipe(id: String, tags: String? = null): Recipe =
        Recipe(
            id = id,
            name = "Sample Meal",
            category = "Dinner",
            tags = tags,
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
            strMeasure20 = null
        )
}
