package com.example.gestioneventos.data.repository

import com.example.gestioneventos.domain.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun addEvent(event: Event, onResult: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val docRef = firestore.collection("users").document(userId).collection("events").document()
        val eventWithId = event.copy(id = docRef.id)
        docRef.set(eventWithId)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getEvents(onData: (List<Event>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).collection("events")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val events = snapshot.toObjects(Event::class.java)
                    onData(events)
                }
            }
    }

    fun updateEvent(event: Event, onResult: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).collection("events").document(event.id)
            .set(event)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun deleteEvent(id: String, onResult: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).collection("events").document(id)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }


}