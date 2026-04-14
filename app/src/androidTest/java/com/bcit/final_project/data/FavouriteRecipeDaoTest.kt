package com.bcit.final_project.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavouriteRecipeDaoTest {
    private lateinit var database: RecipeDatabase
    private lateinit var dao: FavouriteRecipeDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RecipeDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.favouriteRecipeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertAndDeleteFavouriteUpdateQueries() = runBlocking {
        val firstFavourite = sampleEntity(recipeId = "1", savedAtEpochMillis = 10L)
        val newerFavourite = sampleEntity(recipeId = "2", savedAtEpochMillis = 20L)

        dao.saveFavourite(firstFavourite)
        dao.saveFavourite(newerFavourite)

        assertEquals(listOf(newerFavourite, firstFavourite), dao.getAllFavourites().first())
        assertEquals(firstFavourite, dao.getFavouriteById("1"))

        dao.deleteFavouriteById("1")

        assertNull(dao.getFavouriteById("1"))
        assertEquals(listOf(newerFavourite), dao.getAllFavourites().first())
    }

    @Test
    fun upsertReplacesExistingRecipe() = runBlocking {
        dao.saveFavourite(sampleEntity(recipeId = "1", savedAtEpochMillis = 10L))
        dao.saveFavourite(sampleEntity(recipeId = "1", name = "Updated", savedAtEpochMillis = 30L))

        val favourites = dao.getAllFavourites().first()
        assertEquals(1, favourites.size)
        assertEquals("Updated", favourites.single().name)
        assertEquals(30L, favourites.single().savedAtTime)
    }

    private fun sampleEntity(
        recipeId: String,
        name: String = "Sample Meal",
        savedAtEpochMillis: Long
    ): Recipe =
        Recipe(
            id = recipeId,
            name = name,
            category = "Dinner",
            tags = "Simple,Favourite",
            image = "https://example.com/meal.jpg",
            video = "https://example.com/video",
            strIngredient1 = "Rice",
            strIngredient2 = null,
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
            strMeasure2 = null,
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
            savedAtTime = savedAtEpochMillis
        )
}
