package app.bastionkern

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

/** Firebase project is fixed: never connect user data to an arbitrary project. */
object FirebaseProject {
    const val PROJECT_ID = "bastion-sdk"
    fun isConfigured(context: Context): Boolean =
        FirebaseApp.getApps(context).any { it.options.projectId == PROJECT_ID }
}

/**
 * Firebase Authentication owns passwords and sessions. Firestore stores only the
 * user's private profile: never passwords, tokens, roles, entitlements or email.
 */
class FirebaseAccountRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    fun register(email: String, password: String, callback: (Result<Unit>) -> Unit) {
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    callback(Result.failure(task.exception ?: IllegalStateException("Registration failed")))
                    return@addOnCompleteListener
                }
                val user = task.result?.user
                if (user == null) {
                    callback(Result.failure(IllegalStateException("Firebase user missing")))
                    return@addOnCompleteListener
                }
                createProfile(user, callback)
                user.sendEmailVerification()
            }
    }

    private fun createProfile(user: FirebaseUser, callback: (Result<Unit>) -> Unit) {
        val data = hashMapOf<String, Any>(
            "uid" to user.uid,
            "displayName" to "",
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        firestore.collection("users").document(user.uid).set(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) callback(Result.success(Unit))
                else callback(Result.failure(task.exception ?: IllegalStateException("Profile creation failed")))
            }
    }

    fun signIn(email: String, password: String, callback: (Result<Unit>) -> Unit) {
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) callback(Result.success(Unit))
                else callback(Result.failure(task.exception ?: IllegalStateException("Sign-in failed")))
            }
    }

    fun resetPassword(email: String, callback: (Result<Unit>) -> Unit) {
        auth.sendPasswordResetEmail(email.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) callback(Result.success(Unit))
                else callback(Result.failure(task.exception ?: IllegalStateException("Reset failed")))
            }
    }

    fun loadDisplayName(uid: String, callback: (Result<String>) -> Unit) {
        firestore.collection("users").document(uid).get()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    callback(Result.failure(task.exception ?: IllegalStateException("Profile read failed")))
                } else {
                    callback(Result.success(task.result?.getString("displayName").orEmpty()))
                }
            }
    }

    fun updateDisplayName(uid: String, displayName: String, callback: (Result<Unit>) -> Unit) {
        val name = displayName.trim()
        if (name.length > 60) {
            callback(Result.failure(IllegalArgumentException("Name must be at most 60 characters")))
            return
        }
        firestore.collection("users").document(uid).update(
            mapOf(
                "displayName" to name,
                "updatedAt" to FieldValue.serverTimestamp()
            )
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) callback(Result.success(Unit))
            else callback(Result.failure(task.exception ?: IllegalStateException("Profile update failed")))
        }
    }

    fun signOut() = auth.signOut()
}
