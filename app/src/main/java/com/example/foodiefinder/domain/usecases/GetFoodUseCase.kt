package com.example.foodiefinder.domain.usecases

import com.example.foodiefinder.core.widgets.utils.Resource
import com.example.foodiefinder.data.model.Hit
import com.example.foodiefinder.data.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOError
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * A use case class for fetching food recipes.
 */
class GetFoodUseCase @Inject constructor(private val repository: RecipeRepository) {

    fun execute(searchString: String): Flow<Resource<List<Hit>>> = flow {
        try {
            emit(Resource.Loading())
            val movieListResult = repository.getRecipe(searchString)
            if (movieListResult.data != null) {
                emit(Resource.Success(movieListResult.data!!.hits))
            } else {
                emit(Resource.Error("No recipe found"))
            }
        } catch (e: IOError) {
            emit(Resource.Error("No internet connection"))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Error"))
        } catch (e: SocketTimeoutException) {
            emit(Resource.Error("Timeout"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown Error"))
        }
    }




    fun getRecipeDetails(recipeId: String): Flow<Resource<Hit>> = flow {
        try {
            emit(Resource.Loading())
            val detailsResult = repository.getRecipe(recipeId)
            if (detailsResult.data != null) {
            } else {
                emit(Resource.Error("No details found for recipeId: $recipeId"))
            }
        } catch (e: IOError) {
            emit(Resource.Error("No internet connection"))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Error"))
        } catch (e: SocketTimeoutException) {
            emit(Resource.Error("Timeout"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown Error"))
        }
    }

}