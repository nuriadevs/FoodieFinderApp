package com.example.foodiefinder.ui.screens.favorite

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.foodiefinder.R
import com.example.foodiefinder.data.model.MRecipe
import com.example.foodiefinder.data.model.RecipeX
import com.example.foodiefinder.ui.components.FoodDivider
import com.example.foodiefinder.ui.components.appBar.RecipeAppBar
import com.example.foodiefinder.ui.screens.auth.AuthViewModel
import com.example.foodiefinder.ui.screens.home.HomeScreenViewModel
import com.example.foodiefinder.ui.theme.primaryLight
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FoodFavoriteScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {


    Surface(modifier = Modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            FavoriteContent(navController, homeScreenViewModel)
        }
    }


}


@Composable
fun FavoriteContent(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val userState by homeScreenViewModel.userState.observeAsState()
    val recipesState by homeScreenViewModel.recipesState.observeAsState()

    LaunchedEffect(Unit) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        homeScreenViewModel.getUserData(currentUserUid)
        homeScreenViewModel.getAllFavoriteRecipesForCurrentUser(currentUserUid)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        when {
            recipesState?.loading == true -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            recipesState?.e != null -> {
                Text(
                    text = "Error: ${recipesState!!.e?.message}",
                    style = TextStyle(fontSize = 20.sp, color = Color.Red)
                )
            }

            recipesState?.data.isNullOrEmpty() -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.FoodBank,
                        contentDescription = stringResource(id = R.string.how_cook),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.no_favorites),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            else -> {

                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.my_favorites),
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.RestaurantMenu,
                            contentDescription = stringResource(id = R.string.restaurant_icon),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(recipesState?.data ?: emptyList()) { recipe ->
                            RecipeCardFavorite(data = recipe, onItemClick = {})
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun RecipeCardFavorite(
    data: MRecipe,
    onItemClick: (RecipeX) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),

        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            data.label?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            FoodDivider()

            Spacer(modifier = Modifier.height(20.dp))


            HowToCookIcon(
                url = data.url ?: "",
                recipe = data,
                onClick = {},
                viewModel = hiltViewModel()
            )


            Spacer(modifier = Modifier.height(8.dp))


        }
    }
}


@Composable
fun HowToCookIcon(
    url: String,
    onClick: () -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    recipe: MRecipe
) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: return

    if (showDialog.value) {
        AlertDialog(
            textContentColor = MaterialTheme.colorScheme.secondary,
            onDismissRequest = {
                showDialog.value = false
            },
            title = {
                Text(text =  stringResource(id = R.string.confirm_deletion), color = MaterialTheme.colorScheme.primary)
            },
            text = {
                Text( stringResource(id = R.string.text_deletion))
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    recipe.googleRecipeId?.let { viewModel.removeRecipeFromFavorites(it, userId) }
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.clic_cook),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        /*
                        .clickable {

                            url.takeIf { it.isNotBlank() }?.let {
                                val intent = Intent(Intent.ACTION_VIEW, it.toUri())
                                startActivity(context, intent, null)
                            }
                        }
                    */
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = stringResource(id = R.string.link_icon),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {

                            url
                                .takeIf { it.isNotBlank() }
                                ?.let {
                                    val intent = Intent(Intent.ACTION_VIEW, it.toUri())
                                    startActivity(context, intent, null)
                                }
                        }
                        .size(32.dp)
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete_icon),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            showDialog.value = true
                        }
                        .size(32.dp)
                )
            }
        }
    }
}

