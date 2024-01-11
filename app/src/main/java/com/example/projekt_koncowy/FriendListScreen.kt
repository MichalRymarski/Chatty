package com.example.projekt_koncowy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FriendListScreen(private val navController: NavHostController) {

    private var contactList = mutableStateOf(listOf<String>())
    private val user = FirestoreAuth.currentUserNick
    private val docRef = Firebase.firestore.collection("profile").document(user!!)

    @Composable
    fun StartFriendListScreen() {
        setSnapshotListener()
        refreshList()
        FriendListScreenUI()
    }

    private fun setSnapshotListener() {
        docRef.addSnapshotListener { value , error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                contactList.value = value.get("friends") as List<String>
            }
        }
    }

    private fun refreshList() {
        docRef.get()
            .addOnSuccessListener {
                contactList.value = it.get("friends") as List<String>
            }
    }

    @Composable
    fun FriendListScreenUI() {

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
                            //"Navigate" to HERE
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
                            navController.navigate("addFriend")
                        }) {
                        Text(text = "Dodaj")
                    }
                }

                LazyColumn(content = {
                    items(contactList.value.size) { index ->
                        OutlinedButton(
                            border = BorderStroke(1.dp , Color.Black) ,
                            shape = RoundedCornerShape(50) ,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp) ,

                            onClick = {
                                val user = FirestoreAuth.currentUserNick
                                val friend = contactList.value[index]
                                navController.navigate("chat/${user}/${friend}")
                            }) {
                            Text(text = contactList.value[index])
                        }
                    }
                }
                )

            }
        }
    }
}
