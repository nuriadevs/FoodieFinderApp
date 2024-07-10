package com.example.foodiefinder.ui.components.input

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.foodiefinder.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    supportingText: @Composable (() -> Unit)? = null,
    labelId: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: String?,
    passwordValid: Boolean,
    passwordVisibility: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation =
        if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()


    Box(modifier = modifier.fillMaxWidth()) {

        Column {

            OutlinedTextField(
                maxLines = 1,
                minLines = 1,
                value = password,
                onValueChange = { newPassword ->
                    onPasswordChange(newPassword)
                },
                label = { Text(text = labelId) },
                isError = passwordError != null,
                modifier = modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(id = R.string.password_icon),
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = imeAction,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = onAction,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = when {
                        passwordError != null -> Color.Red
                        passwordValid -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.primary
                    },
                    unfocusedBorderColor = when {
                        passwordError != null -> Color.Red
                        passwordValid -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.primary
                    }
                ),
                visualTransformation = visualTransformation,
                trailingIcon = {
                    PasswordVisibilityIcon(passwordVisibility = passwordVisibility)
                }
            )
            Box(modifier = Modifier.height(20.dp)) {
                if (passwordError != null) {
                    supportingText?.invoke()
                }
            }
        }

    }
}

@Composable
fun PasswordVisibilityIcon(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible }) {
        Icon(
            imageVector = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            contentDescription = if (visible) stringResource(id = R.string.password_hide) else stringResource(
                id = R.string.password_show
            ),
            tint = Color.Gray
        )
    }
}

