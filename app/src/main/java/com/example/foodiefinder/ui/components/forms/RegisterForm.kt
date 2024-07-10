package com.example.foodiefinder.ui.components.forms


import NameInput
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.foodiefinder.R
import com.example.foodiefinder.ui.components.SubmitButton
import com.example.foodiefinder.ui.components.input.EmailInput
import com.example.foodiefinder.ui.components.input.PasswordInput

@Composable
fun RegisterForm(
    loading: Boolean,
    nameError: String?,
    emailError: String?,
    passwordError: String?,
    nameValid: Boolean,
    emailValid: Boolean,
    passwordValid: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onDone: (String, String, String) -> Unit
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }


    val keyboardController = LocalSoftwareKeyboardController.current

    var loading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        NameInput(name = name, onNameChange = {
            name = it
            onNameChange(it)
        }, nameError = nameError, nameValid = nameValid,
            trailingIcon = {
                if (nameError != null) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = stringResource(id = R.string.error),
                        tint = Color.Red
                    )
                }
            },

            supportingText = {
                if (nameError != null) {
                    Text(
                        text = nameError,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 3.dp)

                    )
                }
            }
        )


        EmailInput(email = email, onEmailChange = {
            email = it
            onEmailChange(it)
        }, emailError = emailError, emailValid = emailValid,
            trailingIcon = {
                if (emailError != null) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = stringResource(id = R.string.error),
                        tint = Color.Red
                    )
                }
            },

            supportingText = {
                if (emailError != null) {
                    Text(
                        text = emailError,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 3.dp)

                    )
                }
            }
        )


        PasswordInput(
            labelId = stringResource(id = R.string.password),
            password = password,

            supportingText = {
                if (passwordError != null) {
                    Text(
                        text = passwordError,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 3.dp)

                    )
                }
            },
            onPasswordChange = {
                password = it
                onPasswordChange(it)
            }, passwordError = passwordError, passwordValid = passwordValid,
            passwordVisibility = remember { mutableStateOf(passwordVisible) })

        PasswordInput(
            labelId = stringResource(id = R.string.confirm_password),
            password = confirmPassword,
            supportingText = {
                if (confirmPassword != password) {
                    Text(
                        text = stringResource(id = R.string.pass_not_match),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 3.dp)
                    )
                }
            },
            onPasswordChange = {
                confirmPassword = it
            },
            passwordError = if (confirmPassword != password) stringResource(id = R.string.pass_not_match) else null,
            passwordValid = confirmPassword == password,
            passwordVisibility = remember { mutableStateOf(false) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SubmitButton(
            text = stringResource(id = R.string.register),
            loading = loading,
            validInputs = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()

        ) {
            onDone(
                name.trim(),
                email.trim(),
                password.trim()
            )
            keyboardController?.hide()
        }


    }

}




