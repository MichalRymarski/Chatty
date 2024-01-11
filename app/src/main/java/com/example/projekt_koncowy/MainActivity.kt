package com.example.projekt_koncowy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() { //init rzeczy

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
                        val navController = rememberNavController()
                        NavHost(navController = navController ,
                            startDestination = "login" , builder =  {
                                composable("login") {
                                    LoginScreen(navController).LoginScreenUI()
                                }
                                composable("register") {
                                    RegistrationScreen(navController).RegistrationScreenUI()
                                }
                                composable("addFriend") {
                                    AddFriendScreen(navController).AddFriendScreenUI()
                                }
                                composable("friendList") {
                                    FriendListScreen(navController).startFriendListScreen()
                                }
                                composable("awaitingList") {
                                    AwaitingListScreen(navController).startAwaitingLIstScreen()
                                }
                                composable("chat/{user}/{friend}") { backStackEntry ->
                                    val arguments = backStackEntry.arguments
                                    val user = arguments?.getString("user")
                                    val friend = arguments?.getString("friend")
                                    ChatScreen(navController, user, friend!!).StartChatScreenUI()
                                }

                            })


                        navController.navigate("login")


                    }
                }
            }
        }



    }



}

