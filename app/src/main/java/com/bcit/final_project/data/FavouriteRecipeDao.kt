package com.bcit.final_project.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteRecipeDao {
    @Query("SELECT * FROM favourite_recipes ORDER BY savedAtTime DESC")
    fun getAllFavourites(): Flow<List<Recipe>>

    @Query("SELECT * FROM favourite_recipes WHERE recipeId = :recipeId LIMIT 1")
    suspend fun getFavouriteById(recipeId: String): Recipe?

    @Upsert
    suspend fun saveFavourite(entity: Recipe)

    @Query("DELETE FROM favourite_recipes WHERE recipeId = :recipeId")
    suspend fun deleteFavouriteById(recipeId: String)
}
