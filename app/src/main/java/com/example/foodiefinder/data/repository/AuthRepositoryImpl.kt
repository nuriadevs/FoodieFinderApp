package com.example.foodiefinder.data.repository


import com.example.foodiefinder.data.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * A repository implementation for authentication-related operations.
 */
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) :
    AuthRepository {

    /**
     * Signs in a user with the provided email and password.
     *
     * @param email the user's email address.
     * @param password the user's password.
     * @return a Result object containing FirebaseUser on success, or an Exception on failure.
     */

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser?> = suspendCoroutine { continuation ->
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(Result.success(task.result?.user))
            } else {
                continuation.resume(
                    Result.failure(
                        task.exception ?: Exception("Failed to sign in")
                    )
                )
            }
        }
    }

    /**
     * Creates a new user account with the provided name, city, email, and password.
     *
     * @param name the user's name.
     * @param city the user's city.
     * @param email the user's email address.
     * @param password the user's password.
     * @return a Result object indicating success or failure.
     */

    override suspend fun createUserWithEmailAndPassword(
        name: String,
        city: String,
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = MUser(
                userId = auth.currentUser?.uid.toString(),
                displayName = auth.currentUser?.email?.split('@')?.get(0) ?: "",
                name = name,
                email = email,
                avatarUrl = ""
            )
            addUserToFirestore(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Adds a user to Firestore database.
     *
     * @param user the user object to be added.
     * @return a Result object indicating success or failure.
     */

    override suspend fun addUserToFirestore(user: MUser): Result<Unit> {
        return try {
            user.userId?.let { firestore.collection("users").document(it).set(user).await() }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Signs out the current user.
     */

    override fun signOut() {
        auth.signOut()
    }

    /**
     * Checks if a user is currently authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    override fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }


}