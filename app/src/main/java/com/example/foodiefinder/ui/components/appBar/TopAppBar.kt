package com.example.foodiefinder.ui.components.appBar


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults.textContentColor
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.foodiefinder.R
import com.example.foodiefinder.ui.theme.secondaryLight
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foodiefinder.ui.navigation.FoodScreens
import com.example.foodiefinder.ui.screens.auth.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeAppBar(navController: NavController, authViewModel: AuthViewModel = hiltViewModel()) {


    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AlertDialog(
            textContentColor = MaterialTheme.colorScheme.secondary,
            onDismissRequest = {
                showDialog.value = false
            },
            title = {
                Text(text =  stringResource(id = R.string.confirm_logout), color = MaterialTheme.colorScheme.primary)
            },
            text = {
                Text( stringResource(id = R.string.text_logout))
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    authViewModel.logout()
                    navController.navigate(FoodScreens.LoginScreen.name) {
                        popUpTo(FoodScreens.LoginScreen.name) { inclusive = true }
                    }
                }) {
                    Text(stringResource(id = R.string.yes), color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog.value = false
                }) {
                    Text(stringResource(id = R.string.no), color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        TopAppBar(
            windowInsets = WindowInsets(0, 0, 0, 0),

            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            actions = {
                IconButton(onClick = {
                    showDialog.value = true

                }) {
                    Icon(
                        Icons.Filled.Output,
                        contentDescription = stringResource(id = R.string.output),
                        Modifier.size(30.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                navigationIconContentColor = secondaryLight,
            )
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterial3Api
@Composable
fun RecipeAppBarArrow(navController: NavController, onBackArrowClicked: () -> Unit) {

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        CenterAlignedTopAppBar(
            windowInsets = WindowInsets(0, 0, 0, 0),
            title = {
                Text(
                    text = "Recipe",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    onBackArrowClicked()
                }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        stringResource(id = R.string.back_icon),
                        Modifier.size(30.dp)
                    )
                }
            }, actions = {
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                navigationIconContentColor = secondaryLight,
            )
        )
    }
}



