package com.example.projekt_koncowy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistrationScreen(private val navController: NavHostController) {

    @Composable
    fun RegistrationScreenUI() {
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val nick = remember { mutableStateOf("") }


        Surface(
            modifier = Modifier.fillMaxSize()

        ) {
            Column(
                modifier = Modifier.fillMaxSize() ,
                verticalArrangement = Arrangement.Center ,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(text = "Zarejestruj się" , fontSize = 30.sp)

                TextField(
                    value = nick.value ,
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(shape = RoundedCornerShape(40.dp)) ,
                    label = { Text(text = "Nick" , fontSize = 19.sp) } ,
                    onValueChange = {
                        nick.value = it
                    }
                )

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
                        signUp(email , password, nick)
                        //navigate to conversation screen
                    }) {
                    Text(text = "Zarejestruj" , fontSize = 20.sp)
                }
            }
        }
    }


        private fun signUp(email: MutableState<String> , password: MutableState<String>, nick: MutableState<String>) {
            val auth: FirebaseAuth = FirestoreAuth.auth
            auth.createUserWithEmailAndPassword(email.value , password.value).addOnSuccessListener {
                Firebase.firestore.collection("profile")
                    .add(hashMapOf(
                        "email" to email.value ,
                        "password" to password.value,
                        "nick" to nick.value
                    ))
            }.addOnFailureListener {

            }
        }


}