package com.example.eyecadmin

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUp(navController: NavController) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passkey by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Sign-Up Page", fontSize = 32.sp)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = passkey, onValueChange = { passkey = it }, label = { Text("Passkey") })
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (email.isNotEmpty() && password.isNotEmpty() && passkey == "Ke-Shav") {
                createUser(auth, email, password) { success ->
                    if (success) {
                        navController.navigate("home")
                    } else {
                        errorMessage = "Sign-Up failed. Try again."
                    }
                }
            } else {
                errorMessage = "Fields cannot be empty or passkey incorrect."
            }
        }) {
            Text("Sign-Up")
        }

        Spacer(Modifier.height(8.dp))
        errorMessage?.let { Text(it, color = Color.Red) }
        TextButton(onClick = { navController.navigate("login") }) { Text("Already have an account? Login") }
    }
}

fun createUser(auth: FirebaseAuth, email: String, password: String, onResult: (Boolean) -> Unit) {
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Log.d("SignUp", "User registered successfully")
        } else {
            Log.e("SignUp", "Error: ${task.exception?.message}")
        }
        onResult(task.isSuccessful)

    }
}
