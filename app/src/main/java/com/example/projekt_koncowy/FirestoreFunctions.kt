package com.example.projekt_koncowy

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun addFriend(name: String) {

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

fun checkIfFriendExist(name: String , user: String?): Boolean {

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

fun isFriendYou(name: String , user: String?): Boolean {
    var flag: Boolean = false
    if (name == user) {
        flag = true
    }
    return flag
}

 fun createChat(user: String , friend: String) {
    val temp = mutableListOf<String>()
    temp.add("$user: Cześć")
    Firebase.firestore.collection("konwersacje").document("${user}${friend}")
        .set(
            hashMapOf(
                "wiadomosci" to temp
            )
        )
}
fun signUp(email: MutableState<String> , password: MutableState<String> , nick: MutableState<String> , navController: NavHostController) {
    val auth: FirebaseAuth = FirestoreAuth.auth
    auth.createUserWithEmailAndPassword(email.value , password.value).addOnSuccessListener {
        Firebase.firestore.collection("profile").document("${nick.value}")
            .set(
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
    }.addOnFailureListener{
        Log.d("RegistrationScreen", "Nie udalo sie zarejestrowac${it.message}")
    }
}




