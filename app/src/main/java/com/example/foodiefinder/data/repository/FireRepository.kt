package com.example.foodiefinder.data.repository


import android.util.Log
import com.example.foodiefinder.core.exception.DataOrException
import com.example.foodiefinder.data.model.MRecipe
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * A repository class for Firestore database operations.
 */
class FireRepository @Inject constructor(
    private val firestore: FirebaseFirestore,

) {

    /**
     * Retrieves favorite recipes for a specific user.
     *
     * @param uid the user ID.
     * @return a DataOrException object containing a list of MRecipe on success, or an Exception on failure.
     */

    suspend fun getFavoriteRecipesForUser(uid: String): DataOrException<List<MRecipe>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MRecipe>, Boolean, Exception>()
        try {
            val result = firestore.collection("recipes")
                .whereEqualTo("userId", uid)
                .get()
                .await()
            val recipes = result.toObjects(MRecipe::class.java)
            dataOrException.data = recipes
            dataOrException.loading = false
        } catch (e: Exception) {
            dataOrException.e = e
        }
        return dataOrException
    }

    /**
     * Adds a recipe to the user's favorites.
     *
     * @param recipe the recipe object to add.
     * @param userId the user ID.
     * @return true if the operation was successful, false otherwise.
     */

    suspend fun addRecipeToFavorites(recipe: MRecipe, userId: String): Boolean {
        return try {
            val db = FirebaseFirestore.getInstance()
            db.collection("recipes")
                .add(recipe)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Removes a recipe from the user's favorites.
     *
     * @param recipeId the ID of the recipe to remove.
     * @param userId the user ID.
     * @return true if the operation was successful, false otherwise.
     */

    suspend fun removeRecipeFromFavorites(recipeId: String, userId: String): Boolean {
        return try {
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("recipes")
                .whereEqualTo("userId", userId)
                .whereEqualTo("googleRecipeId", recipeId)
                .get()
                .await()

            if (!query.isEmpty) {
                for (document in query.documents) {
                    db.collection("recipes").document(document.id).delete().await()
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Checks if a recipe is marked as a favorite by the user.
     *
     * @param recipeId the ID of the recipe to check.
     * @param userId the user ID.
     * @return true if the recipe is a favorite, false otherwise.
     */

    suspend fun isRecipeFavorite(recipeId: String, userId: String): Boolean {
        return try {
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("recipes")
                .whereEqualTo("userId", userId)
                .whereEqualTo("googleRecipeId", recipeId)
                .get()
                .await()

            !query.isEmpty
        } catch (e: Exception) {
            false
        }
    }

}