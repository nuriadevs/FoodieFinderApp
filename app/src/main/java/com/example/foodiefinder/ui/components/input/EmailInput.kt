package com.example.foodiefinder.ui.components.input


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.foodiefinder.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInput(
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String?,
    emailValid: Boolean,
    labelId: String = "Email",
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
){

            OutlinedTextField(
                trailingIcon = trailingIcon,
                maxLines = 1,
                minLines = 1,
                value = email,
                onValueChange = { newEmail ->
                    onEmailChange(newEmail)
                },
                label = { Text(text = labelId) },
                isError = emailError != null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = stringResource(id = R.string.email_icon),
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = imeAction,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = onAction,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = when {
                        emailError != null -> Color.Red
                        emailValid -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.primary
                    },
                    unfocusedBorderColor = when {
                        emailError != null -> Color.Red
                        emailValid -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.primary
                    }
                )

            )

    Box(modifier = Modifier.height(20.dp)) {
        if (emailError != null) {
            supportingText?.invoke()
        }
    }


}

