package com.example.foodiefinder.ui.screens.home

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodiefinder.core.widgets.utils.Resource
import com.example.foodiefinder.data.model.Hit
import com.example.foodiefinder.data.model.RecipeListEvent
import com.example.foodiefinder.data.model.RecipeListState
import com.example.foodiefinder.domain.usecases.GetFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 *  A view model class for the search screen.
 */
@HiltViewModel
class FoodSearchViewModel  @Inject constructor(private val repository: GetFoodUseCase) : ViewModel() {

    private var selectedRecipe: Hit? = null

    fun getSelectedRecipe(): Hit? {
        return selectedRecipe
    }

    fun setSelectedRecipe(recipe: Hit?) {
        selectedRecipe = recipe
    }


    private val _state = mutableStateOf(RecipeListState())
    val state: State<RecipeListState> = _state

    private var job: Job? = null

    init {
        getRecipeData(_state.value.toString())
    }

    /**
     * Gets the recipe data from the repository.
     */
    private fun getRecipeData(searchString: String) {
        job?.cancel()
        job = repository.execute(searchString).onEach {
            when (it) {
                is Resource.Success -> {
                    _state.value = RecipeListState(recipe = it.data ?: emptyList())
                    Log.d(ContentValues.TAG, "getRecipeData OK: ${it.data}")
                }

                is Resource.Error -> {
                    _state.value = RecipeListState(error = it.message ?: "Error")
                    Log.d(ContentValues.TAG, "getRecipeData ERROR: ${it.data}")
                }

                is Resource.Loading -> {
                    _state.value = RecipeListState(isLoading = true)
                }

            }
        }.launchIn(viewModelScope)
    }

    /**
     * Handles the recipe list events.
     */
    fun onEvent(event: RecipeListEvent) {
        when (event) {
            is RecipeListEvent.Search -> {
                getRecipeData(event.searchString)
            }

        }
    }

    /**
     * Gets the recipe data from the repository.
     */
    fun retry() {
        getRecipeData(_state.value.toString())
    }


}