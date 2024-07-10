package com.example.foodiefinder.data.repository

import com.example.foodiefinder.core.exception.DataOrException
import com.example.foodiefinder.core.widgets.utils.Resource
import com.example.foodiefinder.data.model.Hit
import com.example.foodiefinder.data.model.Recipe
import com.example.foodiefinder.data.network.RecipeApi
import javax.inject.Inject

/**
 * A repository class for making API requests related to recipes.
 */
class RecipeRepository @Inject constructor(private val api: RecipeApi) {


    /**
     * Retrieves a recipe based on the query.
     *
     * @param hitQuery the query string for the recipe.
     * @return a DataOrException object containing a Recipe on success, or an Exception on failure.
     */

    suspend fun getRecipe(hitQuery: String): DataOrException<Recipe, Boolean, Exception> {
        val response = try {
            api.getRecipe(query = hitQuery)
        } catch (e: Exception) {
            return DataOrException(e = e)
        }
        return DataOrException(data = response)
    }


    /**
     * Retrieves detailed information about a recipe based on its URI.
     *
     * @param uri the URI of the recipe.
     * @return a Resource object containing Hit on success, or an error message on failure.
     */

    suspend fun getUriInfo(uri: String): Resource<Hit> {
        val response = try {
            Resource.Loading(data = true)
            api.getRecipeById(uri)
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)

    }

}