package com.example.foodiefinder.ui.screens.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodiefinder.core.exception.DataOrException
import com.example.foodiefinder.data.model.MRecipe
import com.example.foodiefinder.data.repository.FireRepository
import com.example.foodiefinder.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import javax.inject.Inject

/**
 * A view model class for the home screen.
 */
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: FireRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userState =
        MutableLiveData<DataOrException<Map<String, Any>, Boolean, Exception>>()
    val userState: LiveData<DataOrException<Map<String, Any>, Boolean, Exception>> get() = _userState

    val data: MutableState<DataOrException<List<MRecipe>, Boolean, Exception>> =
        mutableStateOf(DataOrException(listOf(), true, Exception("")))


    private val _recipesState =
        MutableLiveData<DataOrException<List<MRecipe>, Boolean, Exception>>()
    val recipesState: LiveData<DataOrException<List<MRecipe>, Boolean, Exception>> get() = _recipesState


    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String> get() = _snackbarMessage


    init {
        getUserData(FirebaseAuth.getInstance().currentUser?.uid)
    }

    /**
     * Gets the user data from the repository.
     */
    fun getUserData(uid: String?) {
        if (uid == null) {
            _userState.value =
                DataOrException(loading = false, e = IllegalArgumentException("User ID is null"))
            return
        }

        viewModelScope.launch {
            _userState.value = DataOrException(loading = true)
            try {
                val result = userRepository.getUserData(uid)
                _userState.value = result
            } catch (e: Exception) {
                _userState.value = DataOrException(e = e)
            }
        }
    }

    /**
     * Gets all favorite recipes for the current user.
     */
    fun getAllFavoriteRecipesForCurrentUser(uid: String?) {
        viewModelScope.launch {
            uid?.let {
                _recipesState.value = DataOrException(loading = true)
                val result = repository.getFavoriteRecipesForUser(uid)
                _recipesState.postValue(result)
            }
        }
    }

    /**
     * Adds a recipe to the user's favorites.
     */
    fun addRecipeToFavorites(recipe: MRecipe, userId: String) {
        viewModelScope.launch {
            val added = repository.addRecipeToFavorites(recipe, userId)
            if (added) {
                _snackbarMessage.postValue("Recipe added to favorites")
                getAllFavoriteRecipesForCurrentUser(userId)
            } else {
               _snackbarMessage.postValue("Failed to add recipe to favorites")
            }
        }
    }

    /**
     * Removes a recipe from the user's favorites.
     */
    fun removeRecipeFromFavorites(recipeId: String, userId: String) {
        viewModelScope.launch {
            val removed = repository.removeRecipeFromFavorites(recipeId, userId)
            if (removed) {
                _snackbarMessage.postValue("Recipe removed from favorites")
                getAllFavoriteRecipesForCurrentUser(userId)
            } else {
                _snackbarMessage.postValue("Failed to remove recipe from favorites")
            }
        }
    }

    /**
     * Checks if a recipe is favorite for the current user.
     */
    suspend fun isRecipeFavorite(recipeId: String, userId: String): Boolean {
        return try {
            repository.isRecipeFavorite(recipeId, userId)
        } catch (e: FirebaseFirestoreException) {
            false
        }
    }

    fun toggleFavorite(recipe: MRecipe, userId: String) {
        viewModelScope.launch {
            val isFavorite = recipe.googleRecipeId?.let { isRecipeFavorite(it, userId) }
            if (isFavorite == true) {
                removeRecipeFromFavorites(recipe.googleRecipeId, userId)
            } else {
                addRecipeToFavorites(recipe, userId)
            }
        }
    }


    suspend fun getImageUrl(avatarUrl: String): String {
        return userRepository.getImageUrl(avatarUrl)
    }


}


