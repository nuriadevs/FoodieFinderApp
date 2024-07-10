package com.example.foodiefinder.data.model

/**
 * A data class representing a user.
 */
data class MUser(
    var userId: String? = null,
    var name: String? = null,
    var displayName: String? = null,
    var city: String? = null,
    var email: String? = null,
    var avatarUrl: String? = null
) {
    fun toMap(): MutableMap<String, String?> {
        return mutableMapOf(
            "userId" to this.userId,
            "name" to this.name,
            "displayName" to this.displayName,
            "city" to this.city,
            "email" to this.email,
            "avatarUrl" to this.avatarUrl
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): MUser {
            return MUser(
                userId = map["userId"] as? String,
                name = map["name"] as? String,
                displayName = map["displayName"] as? String,
                city = map["city"] as? String,
                email = map["email"] as? String,
                avatarUrl = map["avatarUrl"] as? String
            )
        }
    }
}
