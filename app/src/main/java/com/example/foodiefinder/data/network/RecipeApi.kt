package com.example.foodiefinder.data.network

import com.example.foodiefinder.BuildConfig
import com.example.foodiefinder.data.model.Hit
import com.example.foodiefinder.data.model.Recipe
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton


/**
 * A Retrofit interface for making API requests related to recipes.
 */
@Singleton
interface RecipeApi {

    companion object {
        const val API_KEY = BuildConfig.API_KEY
        const val APP_ID = BuildConfig.APP_ID
    }

    /**
     * Retrieves a recipe based on a query.
     */

    @GET(value = "/api/recipes/v2?type=public")
    suspend fun getRecipe(
        @Query("q") query: String,
        @Query("app_id") app_id: String = APP_ID,
        @Query("app_key") app_key: String = API_KEY
    ): Recipe

    /**
     * Retrieves a recipe by its ID.
     */

    @GET(value = "/api/recipes/v2?type=public")
    suspend fun getRecipeById(
        @Path("id") recipeId: String,
        @Query("type") type: String = "public",
        @Query("app_id") app_id: String = BuildConfig.APP_ID,
        @Query("app_key") app_key: String = BuildConfig.API_KEY
    ): Hit

}