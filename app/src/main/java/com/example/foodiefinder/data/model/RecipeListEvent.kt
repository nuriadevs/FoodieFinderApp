package com.example.foodiefinder.data.model

/**
 * A sealed class representing different types of recipe list events.
 */
sealed class RecipeListEvent {
    data class Search(val searchString: String) : RecipeListEvent()
}