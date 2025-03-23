package com.example.eyecadmin

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

object DatabaseOperations {
    private val db = FirebaseFirestore.getInstance()
    fun addMovie(context : Context, movieId: String, title: String, genres: List<String>, tags: List<String>, link: String, thumbnailLink: String) {
        val movie = hashMapOf(
            "movieid" to movieId,
            "title" to title,
            "genres" to genres,
            "tags" to tags,
            "link" to link,
            "thumbnaillink" to thumbnailLink
        )

        db.collection("movies").document(movieId)
            .set(movie)
            .addOnSuccessListener {
                Toast.makeText(context, "Movie added successfully!", Toast.LENGTH_SHORT).show()
                Log.d("Firestore", "Movie added successfully!")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding movie", e)
            }
    }
}
