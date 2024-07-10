package com.example.foodiefinder.ui.components.forms



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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.foodiefinder.R
import com.example.foodiefinder.ui.components.SubmitButton
import com.example.foodiefinder.ui.components.input.EmailInput
import com.example.foodiefinder.ui.components.input.PasswordInput




@Composable
fun LoginForm(
    loading: Boolean,
    emailError: String?,
    passwordError: String?,
    emailValid: Boolean,
    passwordValid: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onDone: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    var loading by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {


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
            })




        PasswordInput(
            labelId = "Password",
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
            passwordVisibility = remember { mutableStateOf(passwordVisible) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        SubmitButton(
            text = stringResource(id = R.string.login),
            loading = loading,
            validInputs = email.isNotEmpty() && password.isNotEmpty(),
            onClick = {
                loading = true
                onDone(email, password)
                loading = false
            }
        )

    }
}