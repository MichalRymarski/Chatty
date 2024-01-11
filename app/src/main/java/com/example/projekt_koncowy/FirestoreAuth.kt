package com.example.projekt_koncowy

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object  FirestoreAuth {
    val auth: FirebaseAuth = Firebase.auth
    private val currentUser = auth.currentUser
    var currentUserMail = currentUser?.email.toString()
    var currentUserNick : String? = null

    suspend fun setCurrentUserNickFirestore(): Boolean = withContext(Dispatchers.IO) {
        var foundNick: String? = null

        try {
            val querySnapshot = Firebase.firestore.collection("profile")
                .whereEqualTo("email", currentUserMail)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                foundNick = document.getString("nick")
            }

            currentUserNick = foundNick

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }




}