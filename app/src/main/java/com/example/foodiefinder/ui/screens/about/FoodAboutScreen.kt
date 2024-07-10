package com.example.foodiefinder.ui.screens.about

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.foodiefinder.R
import com.example.foodiefinder.core.widgets.utils.getSplashImageRes

import com.example.foodiefinder.ui.components.appBar.RecipeAppBar
import com.example.foodiefinder.ui.screens.auth.AuthViewModel

import com.example.foodiefinder.ui.theme.primaryLight
import com.example.foodiefinder.ui.theme.secondaryLight


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FoodAboutScreen(navController: NavController, authViewModel: AuthViewModel = hiltViewModel()) {


    Scaffold(
        modifier = Modifier.padding(top = 16.dp),
        topBar = {
        RecipeAppBar(navController, authViewModel)
    }, content =  {

        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AboutContent()
            }

        }


    }

    )
}


@Composable
fun AboutContent() {
    Column(
        modifier = Modifier
            .padding(top = 15.dp, bottom = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val splashImageRes = getSplashImageRes()

        Image(
            painter = painterResource(splashImageRes),
            contentDescription = stringResource(id = R.string.chef_icon),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(100.dp)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
        )


        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(id = R.string.app_version),
            maxLines = 1,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )


    }


    Column() {

        Column(modifier = Modifier.padding(top = 15.dp)) {
            Text(
                text = stringResource(id = R.string.app_description),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = R.string.description_text),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                lineHeight = 1.15.em,
                textAlign = TextAlign.Justify,
            )

        }
        Spacer(modifier = Modifier.height(15.dp))
        Column() {
            Text(
                text = "Key Features",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold

            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = R.string.app_key_text_1),
                color = MaterialTheme.colorScheme.secondary,
                lineHeight = 1.15.em,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
            )
            Text(
                text = stringResource(id = R.string.app_key_text_2),
                color = MaterialTheme.colorScheme.secondary,
                lineHeight = 1.15.em,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
            )

        }
        Spacer(modifier = Modifier.height(15.dp))
        Column() {
            Text(
                text = "System Requirements",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold

            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = R.string.app_system_text_1),
                color = MaterialTheme.colorScheme.secondary,
                lineHeight = 1.15.em,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
            )
            Text(
                text =  stringResource(id = R.string.app_system_text_2),
                color = MaterialTheme.colorScheme.secondary,
                lineHeight = 1.15.em,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
            )

        }
        Spacer(modifier = Modifier.height(15.dp))
        Column() {
            Text(
                text = stringResource(id = R.string.app_dev_info),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold

            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = R.string.app_dev_text),
                color = MaterialTheme.colorScheme.secondary,
                lineHeight = 1.15.em,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
            )

        }

    }

}


