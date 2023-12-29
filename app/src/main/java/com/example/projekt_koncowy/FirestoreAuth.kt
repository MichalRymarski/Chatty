package com.example.projekt_koncowy

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreAuth {
    val auth: FirebaseAuth = Firebase.auth
    private val currentUser = auth.currentUser

    fun getCurrentUser(): String? {
        return currentUser?.email
    }

    fun getCurrentUserNick(): String? {
        var currentUserNick : String? = null
        Firebase.firestore.collection("profile")
            .whereEqualTo("email",FirestoreAuth().getCurrentUser())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    currentUserNick = document.data["nick"] as String
                }
            }

        return currentUserNick
    }




}