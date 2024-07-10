package com.example.foodiefinder.ui.screens.details

import androidx.lifecycle.ViewModel
import com.example.foodiefinder.core.widgets.utils.Resource
import com.example.foodiefinder.data.model.Hit
import com.example.foodiefinder.data.repository.RecipeRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * A view model class for the details screen.
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: RecipeRepository)

    : ViewModel() {

    /**
     * Gets the URI information from the repository.
     */
    suspend fun getUriInfo(uri: String): Resource<Hit> {

        return repository.getUriInfo(uri)
    }


}

