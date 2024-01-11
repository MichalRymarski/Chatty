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
    var currentUserMail = getCurrentUserEmailFirestore()
    var currentUserNick : String? = null


    private fun getCurrentUserEmailFirestore(): String? {
        return currentUser?.email
    }

    suspend fun setCurrentUserNickFirestore(): Boolean = withContext(Dispatchers.IO) {
        var tempNick: String? = null

        try {
            val querySnapshot = Firebase.firestore.collection("profile")
                .whereEqualTo("email", currentUserMail)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                tempNick = document.getString("nick")
            }

            currentUserNick = tempNick

            true // Return true after setting currentUserNick
        } catch (e: Exception) {
            // Handle any exceptions here, and return false to indicate failure
            e.printStackTrace()
            false
        }
    }




}