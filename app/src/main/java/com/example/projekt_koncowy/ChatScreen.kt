package com.example.projekt_koncowy

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatScreen {

    private var messageList = mutableStateOf(listOf<String>())
    private var user  : String = ""
    private var friend : String = ""
    @Composable
    fun startChatScreenUI(user: String , friend: String) {
        this.user = user
        this.friend = friend

        refreshList()
        ChatScreenUI(user = user , friend = friend)
    }

    @Composable
    fun ChatScreenUI(user: String , friend: String) {

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.Start ,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .clip(shape = RoundedCornerShape(40.dp))
                            .background(Color.LightGray)
                    ) {
                        Text(
                            text = "$friend" ,
                            fontSize = 30.sp ,
                            modifier = Modifier.padding(start = 20.dp)
                        )
                    }
                }


            }
        }
    }

    private fun refreshList() {
        Firebase.firestore.collection("konwersacje")
            .whereArrayContainsAny("ziomki" , listOf("Knurzyn","Mihau"))
            .get()
            .addOnSuccessListener { documents ->
                val newawaitingList = mutableListOf<String>()
                for (document in documents) {
                    val friendsData = document.data["wiadomosci"]
                    if (friendsData is List<*>) {
                        for (data in friendsData) {
                            newawaitingList.add(data.toString())
                        }
                    }
                }
                messageList.value = newawaitingList
                Log.d("ChatScreen" , "messageList: ${messageList.value}")
            }

    }

    @Preview
    @Composable
    fun ChatScreenUIPreview() {
        ChatScreenUI("user" , "friend")
    }

}