package com.example.projekt_koncowy

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class Navigation {
    @Composable
    fun StartNavigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "login") {
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
                FriendListScreen(navController).StartFriendListScreen()
            }
            composable("awaitingList") {
                AwaitingListScreen(navController).StartAwaitingLIstScreen()
            }
            composable("chat/{user}/{friend}") { backStackEntry ->
                val arguments = backStackEntry.arguments
                val user = arguments?.getString("user")
                val friend = arguments?.getString("friend")
                ChatScreen(navController, user, friend!!).StartChatScreenUI()
            }
        }
        navController.navigateUp()
    }
}