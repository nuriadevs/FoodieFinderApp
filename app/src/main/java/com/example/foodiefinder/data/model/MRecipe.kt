package com.example.foodiefinder.data.model

/**
 * A data class representing a recipe.
 */
data class MRecipe(

    val userId: String? = null,
    val googleRecipeId: String? = null,
    val label: String? = null,
    val image: String? = null,
    val calories: Double? = null,
    val mealType: List<String>? = null,
    val cuisineType: List<String>? = null,
    val url: String? = null,

)
