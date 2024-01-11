package com.example.projekt_koncowy

import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Firestore {
    private val db = Firebase.firestore
    public fun removeAwaitingAddFriend(nick: String) {
        val user = FirestoreAuth.currentUserNick
        val updates = mapOf(
            "oczekujace" to FieldValue.arrayRemove(nick) ,
            "friends" to FieldValue.arrayUnion(nick)
        )
        val docRef = db.collection("profile").document(user!!)

        docRef.update(updates)
            .addOnSuccessListener {
            docRef.update("friends" , FieldValue.arrayUnion(user))
                .addOnSuccessListener {
                createChat(user , nick)
            }
        }
    }

    public fun sendMessage(message: String , user: String , friend: String) { //obrzydliwy kod
        db.collection("konwersacje")
            .whereArrayContainsAny("ziomki" , listOf(user , friend))
            .get()
            .addOnSuccessListener { results ->
                addMessageToConv(results , message)
            }
    }

    private fun addMessageToConv(results: QuerySnapshot , message: String) {
        val conversation = results.documents[0]
        val temp = conversation.get("wiadomosci") as MutableList<String>
        temp.add(message)
        db.collection("konwersacje")
            .document(conversation.id)
            .update("wiadomosci" , temp)
    }


    public fun addFriend(nick: String) {
        val currentUserNick = FirestoreAuth.currentUserNick
        db.collection("profile")
            .document(nick)
            .update("oczekujace" , FieldValue.arrayUnion(currentUserNick))
    }


    public fun createChat(user: String , friend: String) {
        val ziomkiList = mutableListOf<String>()
        ziomkiList.add(friend)
        ziomkiList.add(user)
        val temp = mutableListOf<String>()
        temp.add("$user: Cześć")

        db.collection("konwersacje").document("${user}${friend}")
            .set(
                hashMapOf(
                    "wiadomosci" to temp ,
                    "ziomki" to ziomkiList
                )
            )
    }

    public fun signUp(
        email: MutableState<String> ,
        password: MutableState<String> ,
        nick: MutableState<String> ,
        navController: NavHostController
    ) {
        val profileData = hashMapOf(
            "email" to email.value ,
            "password" to password.value ,
            "nick" to nick.value ,
            "friends" to listOf<String>() ,
            "oczekujace" to listOf<String>()
        )
        val auth: FirebaseAuth = FirestoreAuth.auth
        auth.createUserWithEmailAndPassword(email.value , password.value)
            .addOnSuccessListener {
                addProfileDataToFirebase(nick , profileData , navController)
            }
    }

    private fun addProfileDataToFirebase(
        nick: MutableState<String> ,
        profileData: HashMap<String , Any> ,
        navController: NavHostController
    ) {
        db.collection("profile").document("${nick.value}")
            .set(profileData)
            .addOnSuccessListener {
                FirestoreAuth.currentUserNick = nick.value
                navController.navigate("friendList")
            }
    }
}