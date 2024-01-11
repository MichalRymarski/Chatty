package com.example.projekt_koncowy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

class AddFriendScreen(private val navController: NavHostController) {

    @Composable
    fun AddFriendScreenUI() {
        val name = remember { mutableStateOf("") }

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .height(70.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize() ,
                        onClick = {
                            navController.navigate("friendList")
                        }) {
                        Text(text = "Kontakty")
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize() ,
                        onClick = {
                            navController.navigate("awaitingList")

                        }) {
                        Text(text = "Oczekujące")
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize() ,

                        onClick = {
                            //NAVIGATE TO ADD FRIEND
                        }) {
                        Text(text = "Dodaj")
                    }

                }
                Row(
                    modifier = Modifier.padding(top = 30.dp) ,
                    verticalAlignment = Alignment.CenterVertically ,
                    horizontalArrangement = Arrangement.Center ,
                ) {
                    TextField(
                        value = name.value ,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(40.dp))
                            .weight(3f) ,
                        onValueChange = {
                            name.value = it
                        })
                    Button(colors = ButtonDefaults.buttonColors(Color.Red) ,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp) ,
                        onClick = {
                            if (name.value.isNotBlank())
                                addFriend(name.value)
                        }
                    ) {
                        Text(text = "Dodaj")
                    }
                }
            }
        }
    }
}