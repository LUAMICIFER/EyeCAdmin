package com.example.eyecadmin

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// DatabaseOperations.kt
@SuppressLint("StaticFieldLeak")
object DatabaseOperations {
    private val db = FirebaseFirestore.getInstance()
    private var context: Context? = null

    // Call this once when your app starts (in your Application class or MainActivity)
    fun initialize(context: Context) {
        this.context = context.applicationContext
    }

    // Regular function (not suspend) that handles its own coroutine
    fun addMovie(movie: Movie, onComplete: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                db.collection("movies").document(movie.imdbId)
                    .set(movie)
                    .await()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Movie added!", Toast.LENGTH_SHORT).show()
                    onComplete(true)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    onComplete(false)
                }
            }
        }
    }

    fun addSeries(context: Context, series: Series) {
        db.collection("series").document(series.imdbId)
            .set(series)
            .addOnSuccessListener {
                Toast.makeText(context, "Series added!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun addEpisode(context: Context, episode: Episode) {
        val episodeId = "S${episode.seasonNumber}E${episode.episodeNumber}"
        db.collection("series").document(episode.seriesId)
            .collection("episodes").document(episodeId)
            .set(episode)
            .addOnSuccessListener {
                Toast.makeText(context, "Episode added!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
