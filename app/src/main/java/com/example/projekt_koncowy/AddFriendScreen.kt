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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddFriendScreen(private val navController: NavHostController) {


    @Composable
    fun AddFriendScreenUI() {
        val name = remember { mutableStateOf("") }

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column() {
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
                            if (name.value != "")
                                addFriend(name.value)
                        }
                    ) {
                        Text(text = "Dodaj")
                    }
                }
            }

        }
    }

    private fun addFriend(name: String) {

        val currentUserNick = FirestoreAuth.currentUserNick


        if (!checkIfFriendExist(name , currentUserNick) && !isFriendYou(name , currentUserNick)) {
            val docRef = Firebase.firestore.collection("profile")
            val FIND_NICK_QUERY = docRef
                .whereEqualTo("nick" , name)
                .get()
                .addOnSuccessListener {
                    it.documents.map { doc ->
                        doc.reference.update("oczekujace" , FieldValue.arrayUnion(currentUserNick))
                    }
                }
        }
    }


    private fun checkIfFriendExist(name: String , user: String?): Boolean {

        val nick = FirestoreAuth.currentUserNick

        var flag: Boolean = false
        Firebase.firestore.collection("profile")
            .whereEqualTo("email" , FirestoreAuth.currentUserMail)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document.data["friends"] != null) {
                        val friends = document.data["friends"] as List<String>
                        for (friend in friends) {
                            if (friend == name)
                                flag = true
                        }
                    }
                    if (document.data["oczekujace"] != null) {
                        val awaiting = document.data["oczekujace"] as List<String>
                        for (awaited in awaiting) {
                            if (awaited == name)
                                flag = true
                        }
                    }
                }
            }
        return flag

    }

    private fun isFriendYou(name: String , user: String?): Boolean {
        var flag: Boolean = false
        if (name == user) {
            flag = true
        }
        return flag
    }


}