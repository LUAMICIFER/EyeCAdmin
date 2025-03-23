package com.example.eyecadmin

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
fun login(navController: NavController) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login Page", fontSize = 32.sp)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(auth, email, password) { success ->
                    if (success) {
                        navController.navigate("home")
                    } else {
                        errorMessage = "Login failed. Check your email and password."
                    }
                }
            } else {
                errorMessage = "Fields cannot be empty."
            }
        }) {
            Text("Login")
        }

        Spacer(Modifier.height(8.dp))
        errorMessage?.let { Text(it, color = Color.Red) }
        TextButton(onClick = { navController.navigate("sign-up") }) { Text("Create an account (Sign-Up)") }
    }
}

fun signInUser(auth: FirebaseAuth, email: String, password: String, onResult: (Boolean) -> Unit) {
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        onResult(task.isSuccessful)
    }
}
