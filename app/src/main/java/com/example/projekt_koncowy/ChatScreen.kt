package com.example.projekt_koncowy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatScreen(private val navController: NavHostController , private var user: String? , private var friend: String) {

    private var messageList = mutableStateOf(listOf<String>())

    @Composable
    fun StartChatScreenUI() {

        refreshList()
        ChatScreenUI()


    }




    private fun sendMessage(message: String) {

        messageList.value = messageList.value + message

        Firebase.firestore.collection("konwersacje")
            .document("${user}${friend}")
            .set(
                hashMapOf(
                    "wiadomosci" to messageList.value
                )
            )
    }

    private fun refreshList() {
        Firebase.firestore.collection("konwersacje")
            .document("${user}${friend}")
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    messageList.value = it.get("wiadomosci") as List<String>
                }else{
                    messageList.value = listOf<String>()
                }
            }

    }

    @Composable
    fun ChatScreenUI() {


        val message = remember { mutableStateOf("") }


        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) ,

            ) {
            Column {
                Row {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp) ,
                        onClick = {
                            navController.navigate("friendList")
                        }) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            painter =  painterResource(id = R.drawable.back),
                            contentDescription = "",

                            )
                    }
                    Box(
                        modifier = Modifier
                            .weight(4f)
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

                LazyColumn(
                    modifier = Modifier
                        .padding(bottom = 70.dp) ,
                    content = {
                        items(messageList.value.size) { index ->
                            if (messageList.value[index].startsWith(friend)) {

                                Row (
                                    modifier = Modifier.fillMaxWidth() ,
                                    horizontalArrangement = Arrangement.Start
                                ){
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 6.dp , bottom = 6.dp , start = 6.dp)
                                            .wrapContentWidth()
                                            .wrapContentHeight()
                                            .clip(shape = RoundedCornerShape(40.dp))
                                            .background(Color.LightGray)

                                    ) {
                                        Text(
                                            text = messageList.value[index] ,
                                            fontSize = 20.sp ,
                                            modifier = Modifier.padding(6.dp)
                                        )
                                    }
                                }

                            } else {
                                Row (
                                    modifier = Modifier.fillMaxWidth() ,
                                    horizontalArrangement = Arrangement.End
                                ){
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 6.dp , bottom = 6.dp , end = 6.dp)
                                            .wrapContentWidth()
                                            .wrapContentHeight()
                                            .clip(shape = RoundedCornerShape(40.dp))
                                            .background(Color.Cyan)

                                    ) {
                                        Text(
                                            text = messageList.value[index] ,
                                            fontSize = 20.sp ,
                                            modifier = Modifier
                                                .padding(6.dp) ,

                                            )
                                    }
                                }
                            }
                        }
                    })

            }
            Row(
                modifier = Modifier.height(50.dp) ,
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .clip(shape = RoundedCornerShape(40.dp))
                        .background(Color.LightGray)
                        .weight(4f)
                        .height(60.dp)
                ) {
                    TextField(
                        value = message.value ,
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth() ,
                        onValueChange = {
                            message.value = it
                        }
                    )

                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    onClick = {
                        if ( message.value.isNotBlank()) {
                            val temp = "$user: ${message.value}"
                            sendMessage(temp)
                            message.value = ""
                        }
                    }) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter =  painterResource(id = R.drawable.send),
                        contentDescription = "",
                    )

                }
            }

        }
    }


}