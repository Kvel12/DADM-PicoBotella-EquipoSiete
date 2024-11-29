package com.example.dadm.model

data class Challenge(
    val id: String = "", // Changed to String for Firebase ID
    var descripcion: String = "",
    val timestamp: Long = System.currentTimeMillis() // Added for ordering
) {
    // Convert to HashMap for Firebase
    fun toMap(): Map<String, Any> {
        return hashMapOf(
            "id" to id,
            "descripcion" to descripcion,
            "timestamp" to timestamp
        )
    }
}
