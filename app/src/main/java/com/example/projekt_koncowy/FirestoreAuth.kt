package com.example.projekt_koncowy

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object  FirestoreAuth {
    @SuppressLint("StaticFieldLeak")

    val db = Firebase.firestore
    val auth: FirebaseAuth = Firebase.auth
    private val currentUser = auth.currentUser
    var currentUserMail = currentUser?.email.toString()
    var currentUserNick : String? = null

    suspend fun setCurrentUserNickFirestore(): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val document = db.collection("profile")
                .whereEqualTo("email", currentUserMail)
                .limit(1)
                .get()
                .await()
                .documents
                .firstOrNull()

            currentUserNick = document?.getString("nick")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}