package com.example.projekt_koncowy

import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FriendListScreen {

    var contactList = mutableStateOf(listOf<String>())

    @Composable
    fun startFriendListScreen() {
        refreshList()
        FriendListScreenUI()
    }

    @Composable
    fun FriendListScreenUI() {
        Surface(
          modifier = Modifier.fillMaxSize()
        ){
            Column{
                Row (
                    modifier = Modifier
                        .height(70.dp)
                ){
                   Button(
                       modifier = Modifier
                           .weight(1f)
                           .fillMaxSize(),
                       onClick = {
                       //Navigate to HERE
                   }) {
                       Text(text = "Kontakty")
                   }

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        onClick = {
                        //NAVIGATE TO AWAITING FOR ACCEPTANCE
                    }){
                        Text(text = "Oczekujące")
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),

                        onClick = {
                        //NAVIGATE TO ADD FRIEND
                    }){
                        Text(text = "Dodaj")
                    }


                }

                LazyColumn(content = {
                    items(contactList.value.size) { index ->
                        OutlinedButton(
                            border = BorderStroke(1.dp, Color.Black),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),

                            onClick = {
                            //NAVIGATE TO CHAT
                        }) {
                            Text(text = contactList.value[index])
                        }

                    }
                }
                )
            }
        }
        
    }

    private fun refreshList() {
        Firebase.firestore.collection("profile")
            .whereEqualTo("email", FirestoreAuth().getCurrentUser())
            .get()
            .addOnSuccessListener { documents ->
                val newContactList = mutableListOf<String>()
                for (document in documents) {
                    val friendsData = document.data["friends"]
                    if (friendsData is List<*>) {
                        for (data in friendsData) {
                            newContactList.add(data.toString())
                        }
                    }
                }
                contactList.value = newContactList
            }
            .addOnFailureListener {
                Log.d("FriendListScreen", "Nie udalo sie pobrac")
            }.addOnCompleteListener {
                Log.d("FriendListScreen", "contactList: ${contactList.value}")
            }
    }

    @Preview
    @Composable
    fun PreviewFriendListScreenUI() {
        FriendListScreenUI()
    }
}
