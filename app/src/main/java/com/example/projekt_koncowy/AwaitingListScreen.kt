package com.example.projekt_koncowy

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AwaitingListScreen(private val navController: NavHostController) {

    var awaitingList = mutableStateOf(listOf<String>())

    @Composable
    fun startAwaitingLIstScreen() {
        refreshList()
        AwaitingLIstScreenUI()
    }

    @Composable
    fun AwaitingLIstScreenUI() {
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
                            navController.navigate("friendList")
                        }) {
                        Text(
                            text = "Kontakty",
                            fontSize = 15.sp
                        )
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        onClick = {
                            //NAVIGATE TO AWAITING FOR ACCEPTANCE
                        }){
                        Text(
                            text = "Oczekujące",
                            fontSize = 15.sp
                        )
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),

                        onClick = {
                            navController.navigate("addFriend")
                        }){
                        Text(
                            text = "Dodaj",
                            fontSize = 15.sp
                        )
                    }


                }

                LazyColumn(content = {
                    items(awaitingList.value.size) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                                .clip(shape = AbsoluteCutCornerShape(40.dp))
                                .background(Color.LightGray)


                        ) {
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = awaitingList.value[index],
                                    modifier = Modifier.padding(start = 20.dp),
                                    fontSize = 20.sp
                                    )
                                Button(shape = AbsoluteCutCornerShape(40.dp),
                                    onClick = {
                                        addtoFriends(awaitingList.value[index])
                                }) {
                                    Text(text = "ADD")
                                }
                            }




                        }

                    }
                }
                )
            }
        }

    }

    private fun addtoFriends(Nick : String){

        Firebase.firestore.collection("profile")
            .whereEqualTo("email", FirestoreAuth.currentUserMail)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val friendsData = document.data["oczekujace"]
                    if (friendsData is List<*>) {
                        for (data in friendsData) {
                            if(data.toString() == Nick){
                                Firebase.firestore.collection("profile").document(document.id).update("oczekujace", FieldValue.arrayRemove(Nick))
                                Firebase.firestore.collection("profile").document(document.id).update("friends", FieldValue.arrayUnion(Nick))
                            }
                        }
                    }
                }
            }.addOnSuccessListener {
                refreshList()
            }





    }
    private fun refreshList() {
        Firebase.firestore.collection("profile")
            .whereEqualTo("email", FirestoreAuth.currentUserMail)
            .get()
            .addOnSuccessListener { documents ->
                val newawaitingList = mutableListOf<String>()
                for (document in documents) {
                    val friendsData = document.data["oczekujace"]
                    if (friendsData is List<*>) {
                        for (data in friendsData) {
                            newawaitingList.add(data.toString())
                        }
                    }
                }
                awaitingList.value = newawaitingList
            }
            .addOnFailureListener {
                Log.d("AwaitingLIstScreen", "Nie udalo sie pobrac")
            }.addOnCompleteListener {
                Log.d("AwaitingLIstScreen", "awaitingList: ${awaitingList.value}")
            }
    }


}
