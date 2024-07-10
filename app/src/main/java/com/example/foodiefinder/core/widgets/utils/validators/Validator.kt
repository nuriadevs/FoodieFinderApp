package com.example.foodiefinder.core.widgets.utils.validators


/**
 * A utility class for validating user registration and login fields.
 */
class Validator {

    companion object {
        const val MIN_PASSWORD_LENGTH = 6

        /**
         * Validates the registration fields.
         *
         * @param name the user's name.
         * @param email the user's email address.
         * @param password the user's password.
         * @return a map of field names to error messages, or null if no error.
         */

        fun validateRegistrationFields(name: String, email: String, password: String): Map<String, String?> {
            val errors = mutableMapOf<String, String?>()

            val (isValidName, nameError) = isValidName(name)
            if (!isValidName) errors["name"] = nameError
            if (!isValidEmail(email)) errors["email"] = "Invalid email format."
            if (!isValidPassword(password)) errors["password"] = "Password must be at least $MIN_PASSWORD_LENGTH characters"

            return errors
        }

        /**
         * Validates the login fields.
         *
         * @param email the user's email address.
         * @param password the user's password.
         * @return a map of field names to error messages, or null if no error.
         */

        fun validateLoginFields(email: String, password: String): Map<String, String?> {
            val errors = mutableMapOf<String, String?>()

            if (!isValidEmail(email)) errors["email"] = "Invalid email format."

            if (!isValidPassword(password)) errors["password"] = "Password must be at least $MIN_PASSWORD_LENGTH characters"

            return errors
        }


        /**
         * Checks if the name is valid.
         *
         * @param name the user's name.
         * @return a pair containing a boolean indicating validity and an error message if invalid.
         */

        fun isValidName(name: String): Pair<Boolean, String?> {
            val isValid = name.isNotBlank()
            val error = if (!isValid) "Name cannot be empty." else null
            return Pair(isValid, error)
        }

        /**
         * Checks if the email is valid.
         *
         * @param email the user's email address.
         * @return true if the email is valid, false otherwise.
         */

        fun isValidEmail(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        /**
         * Checks if the password is valid.
         *
         * @param password the user's password.
         * @return true if the password meets the minimum length, false otherwise.
         */

        fun isValidPassword(password: String): Boolean {
            return password.length >= MIN_PASSWORD_LENGTH
        }
    }

}