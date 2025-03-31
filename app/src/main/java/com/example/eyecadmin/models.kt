package com.example.eyecadmin

import com.google.firebase.firestore.ServerTimestamp
import java.security.Timestamp

// models/Movie.kt
// Updated Movie data class
data class Movie(
    val imdbId: String,
    val title: String,
    val releaseDate: String, // New field for original release date
    val genres: List<String>,
    val playableLinks: List<String>,
    val thumbnailLink: String,
    val createdAt: Long = System.currentTimeMillis()// Auto-set by Firebase when created
)

// models/Series.kt
data class Series(
    val imdbId: String,
    val title: String,
    val genres: List<String>,
    val thumbnailLink: String,
    val createdAt: Long = System.currentTimeMillis()
)

// models/Episode.kt
data class Episode(
    val seriesId: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val title: String,
    val playableLinks: List<String>,
    val thumbnailLink: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
