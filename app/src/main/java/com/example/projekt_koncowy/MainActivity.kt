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
                val initAppAndNick = async {
                    FirebaseApp.initializeApp(this@MainActivity)
                    FirestoreAuth.setCurrentUserNickFirestore()
                }

                if(initAppAndNick.await()){
                    setContent {
                        Navigation().StartNavigation()

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirestoreAuth.auth.signOut()
    }
}

