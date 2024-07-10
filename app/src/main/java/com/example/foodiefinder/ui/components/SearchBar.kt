package com.example.foodiefinder.ui.components


import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import  androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.foodiefinder.R


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchBarExample(
    query: String,
    onQueryChange: (String) -> Unit,
    searchQueryState: MutableState<String>
) {
    var text by remember { mutableStateOf(query) }
    var active by remember { mutableStateOf(false) }
    val searchHistory = remember { mutableStateListOf("") }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        SearchBar(
            windowInsets = WindowInsets(0),
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth(),
            query = text,
            onQueryChange = {
                text = it
                onQueryChange(it)
                searchQueryState.value = it
            },
            onSearch = {
                searchHistory.add(text)
                active = false
            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = {
                Text(text = stringResource(id = R.string.search_by_recipe))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search_icon)
                )
            },
            trailingIcon = {
                if (active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (text.isNotEmpty()) {
                                text = ""
                            } else {
                                active = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close_icon)
                    )
                }
            }

        ) {

            searchHistory.forEach {
                if (it.isNotEmpty()) {
                    Row(modifier = Modifier.padding(all = 14.dp)) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = stringResource(id = R.string.history_icon)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = it)
                    }
                }
            }

            FoodDivider()
            Text(
                modifier = Modifier
                    .padding(all = 14.dp)
                    .fillMaxWidth()
                    .clickable {
                        searchHistory.clear()
                    },
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                text = stringResource(id = R.string.clear_history)
            )
        }

    }

}

