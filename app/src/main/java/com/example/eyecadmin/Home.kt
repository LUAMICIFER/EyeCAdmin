package com.example.eyecadmin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class Genre(val genreName: String) {
    ACTION("action"),
    ADVENTURE("adventure"),
    THRILLER("thriller"),
    SCIFI("sci-fi"),
    HORROR("horror"),
    COMEDY("comedy"),
    DRAMA("drama"),
    ROMANCE("romance"),
    ANIMATION("animation"),
    DOCUMENTARY("documentary"),
    FANTASY("fantasy"),
    ADULT("adult"),
    FAMILY("family")
}

@Composable
fun home() {
    var iscreateMovie by remember { mutableStateOf(false) }
    var isupdate by remember { mutableStateOf(false) }
    var iscreateSeries by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {
        Column {
            Row {
                Button(onClick = { iscreateMovie = !iscreateMovie }) { Text("Create") }
                Button(onClick = { isupdate = !isupdate }) { Text("Update") }
            }
        }

        if (iscreateMovie) {
            var MovieId by remember { mutableStateOf("") }
            var Title by remember { mutableStateOf("") }
            var Link by remember { mutableStateOf("") }
            var ThumbnailLink by remember { mutableStateOf("") }
            var tags by remember { mutableStateOf(listOf<String>()) }
            var selectedGenres by remember { mutableStateOf(listOf<Genre>()) }
            var newTag by remember { mutableStateOf("") }

            Column(
                Modifier
                    .height(550.dp)
                    .width(350.dp)
                    .background(Color.LightGray)
                    .padding(16.dp)
                , horizontalAlignment = Alignment.CenterHorizontally
                , verticalArrangement = Arrangement.Center) {
                Text("Create Movie", Modifier.padding(bottom = 16.dp), fontSize = 24.sp, color = Color.Unspecified)
                OutlinedTextField(
                    value = MovieId,
                    onValueChange = { MovieId = it },
                    label = { Text("MovieId") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = Title,
                    onValueChange = { Title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = Link,
                    onValueChange = { Link = it },
                    label = { Text("Link") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ThumbnailLink,
                    onValueChange = { ThumbnailLink = it },
                    label = { Text("Thumbnail Link") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = newTag,
                        onValueChange = { newTag = it },
                        label = { Text("Add Tag") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = {
                        if (newTag.isNotEmpty()) {
                            tags = tags + newTag
                            newTag = ""
                        }
                    }) {
                        Text("Add")
                    }
                }

                Text("Tags: ${tags.joinToString(", ")}")

                GenreInputDropdown(onGenresSelected = { genres ->
                    selectedGenres = genres
                })

                Text("Selected Genres: ${selectedGenres.joinToString(", ") { it.genreName }}")
                Row(horizontalArrangement = Arrangement.SpaceEvenly){
                    Button(onClick = { iscreateMovie = !iscreateMovie }) { Text("Cancel") }
                    Button(onClick = {
                        iscreateMovie = !iscreateMovie
                        if (MovieId.isNotEmpty() && Title.isNotEmpty() && Link.isNotEmpty() && ThumbnailLink.isNotEmpty() && selectedGenres.isNotEmpty())
                            DatabaseOperations.addMovie(
                                context,
                                MovieId,
                                Title,
                                selectedGenres.map { it.genreName },
                                tags,
                                Link,
                                ThumbnailLink
                            )
                    }) {
                        Text("Create")
                    }
                }
            }


        }
    }
}
@Composable
fun GenreInputDropdown(onGenresSelected: (List<Genre>) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedGenres by remember { mutableStateOf(listOf<Genre>()) }
    var selectedGenresString by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = selectedGenresString,
            onValueChange = {},
            label = { Text("Select Genres") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            Genre.values().forEach { genre ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = selectedGenres.contains(genre),
                                onCheckedChange = { isChecked ->
                                    selectedGenres = if (isChecked) {
                                        selectedGenres + genre
                                    } else {
                                        selectedGenres.filter { it != genre }
                                    }
                                    selectedGenresString = selectedGenres.joinToString(", ") { it.genreName }
                                    onGenresSelected(selectedGenres)
                                }
                            )
                            Text(text = genre.genreName)
                        }
                    },
                    onClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun homePreview() {
    home()
}

