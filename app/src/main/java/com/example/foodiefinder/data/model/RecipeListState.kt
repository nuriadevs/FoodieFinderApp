package com.example.foodiefinder.data.model


/**
 * A data class representing the state of a recipe list.
 */
data class RecipeListState(
    val isLoading: Boolean = false,
    val recipe:  List<Hit> = emptyList(),
    val error: String = "",
    val searchString: String = "flan"
)