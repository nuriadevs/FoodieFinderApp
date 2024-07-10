package com.example.foodiefinder.data.repository

import com.example.foodiefinder.data.model.MUser
import com.google.firebase.auth.FirebaseUser

/**
 * A repository interface for authentication-related operations.
 */
interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser?>
    suspend fun createUserWithEmailAndPassword(name: String, city: String, email: String, password: String): Result<Unit>
    suspend fun addUserToFirestore(user: MUser): Result<Unit>
    fun signOut()
    fun isUserAuthenticated(): Boolean
}