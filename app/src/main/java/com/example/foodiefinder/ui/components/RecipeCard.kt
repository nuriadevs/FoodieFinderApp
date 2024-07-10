package com.example.foodiefinder.ui.components


import android.content.ContentValues.TAG
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.foodiefinder.R
import com.example.foodiefinder.data.model.Hit
import com.example.foodiefinder.data.model.RecipeX
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale


@Composable
fun RecipeCardLittle(
    data: Hit,
    searchQueryState: String,
    onItemClick: (RecipeX) -> Unit
) {

    var isFavorite by remember { mutableStateOf(false) }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid

    LaunchedEffect(data.recipe.uri) {
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val dbCollection = db.collection("recipes")

            dbCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("googleRecipeId", data.recipe.uri)
                .get()
                .addOnSuccessListener { documents ->
                    isFavorite = !documents.isEmpty
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error", exception)
                }
        }
    }


    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onItemClick(data.recipe)
            },
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        shape = RoundedCornerShape(8.dp)
    ) {


        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .size(100.dp)
        ) {

            Row {

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data.recipe.image)
                        .build(),
                    contentDescription = stringResource(id = R.string.image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp),
                )

            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 5.dp)
                    .fillMaxWidth()
                , verticalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {


                    Text(
                        text = data.recipe.mealType[0].replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
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


                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = data.recipe.label,
                            maxLines = 2,
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                fontWeight = FontWeight.Bold
                            ),
                                    color = MaterialTheme.colorScheme.secondary,

                        )

                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = data.recipe.calories.toInt().toString(),
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.alignByBaseline()
                        )
                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = stringResource(id = R.string.calories),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.alignByBaseline()
                        )

                    }

                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxWidth()

                    ) {
                        Text(
                            text = data.recipe.cuisineType[0].replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            } + stringResource(id = R.string.cuisine),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

            }

        }

    }
}

