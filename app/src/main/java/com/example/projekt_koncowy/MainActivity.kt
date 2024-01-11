package com.example.projekt_koncowy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            launch {
                val initFire = async {
                    FirebaseApp.initializeApp(this@MainActivity)
                    FirestoreAuth.auth.signOut()
                }
                val intiNick = async {
                    FirestoreAuth.setCurrentUserNickFirestore()
                }

                if(intiNick.await()){
                    setContent {
                        Navigation().StartNavigation()

                    }
                }
            }
        }
    }
}

