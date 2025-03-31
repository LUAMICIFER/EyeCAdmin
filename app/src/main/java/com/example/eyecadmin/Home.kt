package com.example.eyecadmin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    var showMovieDialog by remember { mutableStateOf(false) }
    var showSeriesDialog by remember { mutableStateOf(false) }
    var showEpisodeDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { showMovieDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Movie")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { showSeriesDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Series")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { showEpisodeDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Episode")
        }

        if (showMovieDialog) {
            AddMovieDialog(
                onDismiss = { showMovieDialog = false },
                onConfirm = { movie ->
                    // This now properly handles the database operation
                    DatabaseOperations.addMovie(
//                        context = LocalContext.current,
                        movie = movie,
                        onComplete = { success ->
                            if (success) {
                                showMovieDialog = false
                                // Optional: refresh your movie list here
                            }
                        }
                    )
                }
            )
        }
//        if (showSeriesDialog) {
//            AddMovieDialog(
//                onDismiss = { showSeriesDialog = false },
//                onConfirm = { movie ->
//                    DatabaseOperations.addMovie(LocalContext.current, movie)
//                }
//            )
//        }
//
//        if (showEpisodeDialog) {
//            AddMovieDialog(
//                onDismiss = { showEpisodeDialog = false },
//                onConfirm = { movie ->
//                    DatabaseOperations.addMovie(LocalContext.current, movie)
//                }
//            )
//        }

    }
}
@Composable
fun AddMovieDialog(
    onDismiss: () -> Unit,
    onConfirm: (Movie) -> Unit
) {
    var imdbId by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var thumbnailLink by remember { mutableStateOf("") }
    var releaseDate by remember { mutableStateOf("") } // New release date field
    var selectedGenres by remember { mutableStateOf(listOf<Genre>()) }
    var playableLinks by remember { mutableStateOf(listOf<String>()) }
    var newLink by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Movie") },
        text = {
            Column() {
                // Basic Info Fields
                OutlinedTextField(
                    value = imdbId,
                    maxLines = 1,
                    onValueChange = { imdbId = it },
                    label = { Text("IMDB ID") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = title,
                    maxLines = 1,
                    onValueChange = { title = it },
                    label = { Text("Movie Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                // New Release Date Field
                OutlinedTextField(
                    value = releaseDate,
                    maxLines = 1,
                    onValueChange = { releaseDate = it },
                    label = { Text("Release Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("2020-05-15") }
                )

                OutlinedTextField(
                    value = thumbnailLink,
                    maxLines = 1,
                    onValueChange = { thumbnailLink = it },
                    label = { Text("Thumbnail URL") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Genre Selection
                GenreSelector(
                    selectedGenres = selectedGenres,
                    onGenresChanged = { selectedGenres = it }
                )
                // Playable Links Section
                Text("Playable Links", style = MaterialTheme.typography.labelLarge)

                Row(Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = newLink,
                        onValueChange = { newLink = it },
                        label = { Text("New Link URL") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            if (newLink.isNotBlank()) {
                                playableLinks = playableLinks + newLink
                                newLink = ""
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Add")
                    }
                }

                // Display added links
                LazyColumn {
                    items(playableLinks) { link ->
                        LinkItem(
                            link = link,
                            onRemove = {
                                playableLinks = playableLinks - link
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val movie = Movie(
                        imdbId = imdbId,
                        title = title,
                        releaseDate = releaseDate,
                        genres = selectedGenres.map { it.genreName },
                        playableLinks = playableLinks,
                        thumbnailLink = thumbnailLink
                    )
                    onConfirm(movie)
                }
            ) {
                Text("Add Movie")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
// Simplified Link Item
@Composable
fun LinkItem(link: String, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(link.take(60) + if (link.length > 60) "..." else "",
                modifier = Modifier.weight(1f))

            Button(onClick = onRemove) {
                Text("Remove")
            }
        }
    }
}
@Composable
fun GenreSelector(
    selectedGenres: List<Genre>,
    onGenresChanged: (List<Genre>) -> Unit
) {
    Column{
        Text("Select Genres", style = MaterialTheme.typography.labelLarge)
        Genre.values().forEach { genre ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    onGenresChanged(
                        if (selectedGenres.contains(genre)) {
                            selectedGenres - genre
                        } else {
                            selectedGenres + genre
                        }
                    )
                }
            ) {
                Checkbox(
                    checked = selectedGenres.contains(genre),
                    onCheckedChange = null // handled by Row click
                )
                Text(genre.genreName)
            }
        }
    }
}
//validator for movie
fun validateMovieInput(
    imdbId: String,
    title: String,
    releaseDate: String,
    thumbnailLink: String,
    genres: List<Genre>,
    links: List<String>
): Boolean {
    return imdbId.isNotBlank() &&
            title.isNotBlank() &&
            releaseDate.isNotBlank() && // Validate release date exists
            thumbnailLink.isNotBlank() &&
            genres.isNotEmpty() &&
            links.isNotEmpty()
}

//@Composable
//fun GenreInputDropdown(onGenresSelected: (List<Genre>) -> Unit) {
//    var expanded by remember { mutableStateOf(false) }
//    var selectedGenres by remember { mutableStateOf(listOf<Genre>()) }
//    var selectedGenresString by remember { mutableStateOf("") }
//
//    Column {
//        OutlinedTextField(
//            value = selectedGenresString,
//            onValueChange = {},
//            label = { Text("Select Genres") },
//            readOnly = true,
//            trailingIcon = {
//                Icon(
//                    imageVector = Icons.Filled.ArrowDropDown,
//                    contentDescription = "Dropdown",
//                    modifier = Modifier.clickable { expanded = !expanded }
//                )
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { expanded = !expanded }
//        )
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Genre.values().forEach { genre ->
//                DropdownMenuItem(
//                    text = {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Checkbox(
//                                checked = selectedGenres.contains(genre),
//                                onCheckedChange = { isChecked ->
//                                    selectedGenres = if (isChecked) {
//                                        selectedGenres + genre
//                                    } else {
//                                        selectedGenres.filter { it != genre }
//                                    }
//                                    selectedGenresString = selectedGenres.joinToString(", ") { it.genreName }
//                                    onGenresSelected(selectedGenres)
//                                }
//                            )
//                            Text(text = genre.genreName)
//                        }
//                    },
//                    onClick = {}
//                )
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun homePreview() {
//    home()
//}
//
