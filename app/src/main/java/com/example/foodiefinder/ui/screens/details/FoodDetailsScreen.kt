package com.example.foodiefinder.ui.screens.details

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrunchDining
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flatware
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.example.foodiefinder.R
import com.example.foodiefinder.data.model.MRecipe
import com.example.foodiefinder.data.model.RecipeX
import com.example.foodiefinder.ui.components.FoodDivider
import com.example.foodiefinder.ui.components.appBar.RecipeAppBarArrow
import com.example.foodiefinder.ui.navigation.FoodScreens
import com.example.foodiefinder.ui.screens.home.HomeScreenViewModel
import com.example.foodiefinder.ui.theme.primaryLight
import com.example.foodiefinder.ui.theme.secondaryLight


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FoodDetailsScreen(
    recipe: RecipeX,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navController: NavController
) {


    Scaffold(
        modifier = Modifier.padding(top = 16.dp),
        topBar = {
            RecipeAppBarArrow(
                navController = navController
            ) { navController.navigate(FoodScreens.FoodHomeScreen.name) }

        },
        content = {
            Surface(
                modifier = Modifier.padding(20.dp)

            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 60.dp)
                ) {

                    RecipeCard(
                        recipe,
                        onItemClick = {},
                        navController = navController, viewModel = viewModel
                    )

                    Spacer(Modifier.height(15.dp))
                   CardWithIconAndTitleIngredients(recipe)

                }

            }

        }
    )
}



@Composable
fun RecipeCard(
    recipe: RecipeX,
    navController: NavController,
    viewModel: HomeScreenViewModel,
    onItemClick: (RecipeX) -> Unit
) {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: return

    var isFavorite by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val snackbarMessage by viewModel.snackbarMessage.observeAsState()

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(recipe.uri) {
        isFavorite = viewModel.isRecipeFavorite(recipe.uri, userId)
    }

    Box {
        OutlinedCard(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier.clickable {
                val mRecipe = MRecipe(
                    userId = userId,
                    googleRecipeId = recipe.uri,
                    label = recipe.label,
                    image = recipe.image,
                    calories = recipe.calories,
                    mealType = recipe.mealType,
                    cuisineType = recipe.cuisineType,
                    url = recipe.url
                )

                viewModel.toggleFavorite(mRecipe, userId)
                isFavorite = !isFavorite
                onItemClick(recipe)
            },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, primaryLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(recipe.image)
                            .build(),
                        contentDescription = stringResource(id = R.string.image),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(180.dp)
                    )
                }

                FoodDivider()

                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = recipe.mealType[0].replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                            },
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Start
                        )
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = stringResource(id = R.string.favorite_icon),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = recipe.label,
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = recipe.calories.toInt().toString(),
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.alignByBaseline()
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = "Calories",
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.alignByBaseline()
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = recipe.cuisineType[0].replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                                    } + " cuisine",
                                    style = TextStyle(
                                        fontSize = MaterialTheme.typography.labelLarge.fontSize,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    color = MaterialTheme.colorScheme.secondary
                                )

                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = recipe.dishType[0].replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                                    },
                                    maxLines = 1,
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { data ->
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    actionColor = MaterialTheme.colorScheme.secondary,
                    snackbarData = data
                )
            }
        )
    }
}


@Composable
fun CardWithIconAndTitleIngredients(
    recipe: RecipeX

) {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {


            Text(
                text = stringResource(id = R.string.main_ingredients),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.secondary
            )

            Icon(
                modifier = Modifier
                    .size(28.dp),
                imageVector = Icons.Default.Flatware,
                contentDescription = stringResource(id = R.string.flatware),
                tint = MaterialTheme.colorScheme.primary
            )

        }
        FoodDivider(thickness = 2.dp)

        Spacer(modifier = Modifier.height(5.dp))

        LazyColumn(modifier = Modifier.padding(top = 5.dp, bottom = 25.dp)) {
            items(recipe.ingredientLines) { ingredient ->
                Text(
                    text = "- $ingredient",
                    modifier = Modifier.padding(5.dp),

                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Normal
                    ),
                    color = MaterialTheme.colorScheme.secondary

                )
            }

        }

    }

}


