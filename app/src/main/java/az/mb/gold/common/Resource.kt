package az.mb.gold.common

import com.google.firebase.firestore.DocumentSnapshot

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val lastVisible: DocumentSnapshot? = null  // Keep this for pagination
) {
    // Success case includes data and lastVisible for pagination
    class Success<T>(data: T, lastVisible: DocumentSnapshot? = null) : Resource<T>(data, lastVisible = lastVisible)

    // Error case includes a message and optionally some data (if applicable)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    // Loading case represents a loading state and optionally can include data
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
