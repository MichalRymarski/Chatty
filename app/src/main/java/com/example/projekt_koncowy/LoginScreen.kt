package com.example.projekt_koncowy

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LoginScreen(private val navController: NavHostController) {

    @Composable
    fun LoginScreenUI() {
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize() ,
                verticalArrangement = Arrangement.Center ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Zaloguj się" , fontSize = 30.sp)
                TextField(
                    value = email.value ,
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(shape = RoundedCornerShape(40.dp)) ,
                    label = { Text(text = "Email" , fontSize = 19.sp) } ,
                    onValueChange = {
                        email.value = it
                    }
                )

                TextField(
                    value = password.value ,
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(shape = RoundedCornerShape(40.dp)) ,
                    label = { Text(text = "Hasło" , fontSize = 19.sp) } ,
                    onValueChange = {
                        password.value = it
                    }
                )

                Button(
                    modifier = Modifier.padding(16.dp) ,
                    onClick = {
                        if (email.value != "" && password.value != "") {
                            signIn(email , password)
                        }
                    }) {
                    Text(text = "Zaloguj" , fontSize = 20.sp)
                }

                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .clip(shape = RoundedCornerShape(40.dp))
                        .background(Color.LightGray)
                ) {
                    Text(text = "Nie masz konta?" , fontSize = 20.sp)
                }

                Button(
                    modifier = Modifier.padding(16.dp) ,
                    onClick = {
                        navController.navigate("register")

                    }) {
                    Text(text = "Zarejestruj" , fontSize = 20.sp)
                }

            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun signIn(email: MutableState<String> , password: MutableState<String>) {
        val auth: FirebaseAuth = FirestoreAuth.auth
        auth.signInWithEmailAndPassword(email.value , password.value)
            .addOnSuccessListener {
                FirestoreAuth.currentUserMail = email.value
                runBlocking {
                    launch {
                        val intiNick = async {
                            FirestoreAuth.setCurrentUserNickFirestore()
                        }
                        if (intiNick.await()) {
                            navController.navigate("friendList")
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(navController.context , "Niepoprawne dane" , Toast.LENGTH_SHORT).show()
            }
    }


}