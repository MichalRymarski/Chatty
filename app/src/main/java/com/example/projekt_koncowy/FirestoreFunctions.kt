package com.example.projekt_koncowy

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


fun removeAwaitingAddFriend(nick: String) {

    val user = FirestoreAuth.currentUserNick
    val db = Firebase.firestore
    val updates = mapOf(
        "oczekujace" to FieldValue.arrayRemove(nick) ,
        "friends" to FieldValue.arrayUnion(nick)
    )

    db.collection("profile").document(user!!).update(updates).addOnSuccessListener {
        db.collection("profile").document(nick).update("friends" , FieldValue.arrayUnion(user)).addOnSuccessListener {
            createChat(user , nick)
        }
    }

}


fun sendMessage(message: String , user: String , friend: String) { //obrzydliwy kod

    val db = Firebase.firestore

    db.collection("konwersacje").whereArrayContainsAny("ziomki" , listOf(user , friend)).get().addOnSuccessListener {
        val temp = it.documents[0].get("wiadomosci") as MutableList<String>
        temp.add(message)
        db.collection("konwersacje").document(it.documents[0].id).update("wiadomosci" , temp)
    }

}

fun addFriend(nick: String) {

    val currentUserNick = FirestoreAuth.currentUserNick
    val db = Firebase.firestore

    db.collection("profile").document(nick).update("oczekujace" , FieldValue.arrayUnion(currentUserNick))
        .addOnSuccessListener {
            Log.d("FriendListScreen" , "Dodano do oczekujacych")
        }

}


fun createChat(user: String , friend: String) {

    val ziomkiList = mutableListOf<String>()
    ziomkiList.add(friend)
    ziomkiList.add(user)
    val temp = mutableListOf<String>()
    temp.add("$user: Cześć")
    Firebase.firestore.collection("konwersacje").document("${user}${friend}").set(
        hashMapOf(
            "wiadomosci" to temp ,
            "ziomki" to ziomkiList
        )
    )
}

fun signUp(email: MutableState<String> , password: MutableState<String> , nick: MutableState<String> , navController: NavHostController) {
    val auth: FirebaseAuth = FirestoreAuth.auth
    auth.createUserWithEmailAndPassword(email.value , password.value)
        .addOnSuccessListener {
            Firebase.firestore.collection("profile").document("${nick.value}").set(
                hashMapOf(
                    "email" to email.value ,
                    "password" to password.value ,
                    "nick" to nick.value ,
                    "friends" to listOf<String>() ,
                    "oczekujace" to listOf<String>()
                )
            ).addOnSuccessListener {
                FirestoreAuth.currentUserNick = nick.value
                navController.navigate("friendList")
            }
        }
        .addOnFailureListener {
            Log.d("RegistrationScreen" , "Nie udalo sie zarejestrowac${it.message}")
        }
}
