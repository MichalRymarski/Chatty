package com.example.projekt_koncowy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.thread
import kotlin.math.log


class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        FirebaseApp.initializeApp(this)
        auth = Firebase.auth


        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("chuj" , "Zalogowany")
        } else {
            Log.d("chuj" , "Nie zalogowany")
        }



        setContent {
            Surface(modifier = Modifier.fillMaxSize() , color = MaterialTheme.colorScheme.background) {
                Row {
                    Button(onClick = {
                        auth.createUserWithEmailAndPassword("mihau123123@gmail.com" , "qwqwqw123").addOnSuccessListener {
                            Log.d("chuj" , "Udalo sie stworzyc")
                            Firebase.firestore.collection("profile")
                                .add(
                                    hashMapOf(
                                        "nick" to "Mihau" ,
                                        "email" to "mihau123123@gmail.com" ,
                                        "password" to "qwqwqw123"
                                    )
                                ).addOnSuccessListener {
                                    Log.d("chuj" , "Udalo sie stworzyc")
                                }.addOnFailureListener {
                                    Log.d("chuj" , "Nie udalo sie stworzyc")
                                }

                        }.addOnFailureListener {
                            Log.d("chuj" , "Nie udalo sie stworzyc")
                        }
                    }) {
                        Text(text = "stworz uzytkownika")
                    }
                    Button(onClick = {
                        auth.signInWithEmailAndPassword("mihau123123@gmail.com" , "qwqwqw123").addOnSuccessListener {
                            Log.d("chuj" , "Udalo sie zalogowac")
                            Firebase.firestore.collection("profile")
                                .whereEqualTo("email" , "mihau123123@gmail.com")
                                .get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        Log.d("chuj" , "${document.id} => ${document.data}")
                                    }
                                }
                        }.addOnFailureListener {
                            Log.d("chuj" , "Nie udalo sie zalogowac")
                        }
                    }) {
                        Text(text = "zaloguj uzytkownika")
                    }

                }


            }
        }
    }
}

