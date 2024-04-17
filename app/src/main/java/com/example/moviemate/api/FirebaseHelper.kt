package com.example.moviemate.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


// Check if movie is in user collection
suspend fun isMovieInUserCol(
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    collectionName: String,
    movieID: Int
): Boolean {
    val userEmail = auth.currentUser!!.email.toString()
    val docRef = db.collection("users")
        .document(userEmail)
        .collection(collectionName)
        .document(movieID.toString())

    val docSnapshot = docRef.get().await()
    return docSnapshot.exists()
}


// Get reference to document in user collection
fun getDocRefToUserCol(
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    collectionName: String,
    movieID: Int,
): DocumentReference {
    val userEmail = auth.currentUser!!.email.toString()

    return db.collection("users")
        .document(userEmail)
        .collection(collectionName)
        .document(movieID.toString())
}