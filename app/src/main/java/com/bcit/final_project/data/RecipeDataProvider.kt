package com.bcit.final_project.data

import android.content.Context
import androidx.room.Room

object RecipeDataProvider {
    @Volatile
    private var databaseInstance: RecipeDatabase? = null

    @Volatile
    private var repositoryInstance: RecipeRepository? = null

    private fun database(context: Context): RecipeDatabase =
        databaseInstance ?: synchronized(this) {
            databaseInstance ?: Room.databaseBuilder(
                context.applicationContext,
                RecipeDatabase::class.java,
                "recipes.db"
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { databaseInstance = it }
        }

    fun recipeRepository(context: Context): RecipeRepository =
        repositoryInstance ?: synchronized(this) {
            repositoryInstance ?: RecipeRepository(
                client = HttpProvider.client,
                favouriteRecipeDao = database(context).favouriteRecipeDao()
            ).also { repositoryInstance = it }
        }
}
