package app.bastionkern

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/** An accessible account panel; map browsing remains available without signing in. */
@Composable
fun FirebaseAccountPanel() {
    val context = LocalContext.current
    val configured = remember(context) { FirebaseProject.isConfigured(context) }
    if (!configured) {
        Column(Modifier.fillMaxWidth().padding(24.dp)) {
            Text("BASTION-KERN Account", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))
            Text("Firebase is not connected to this build. Register app.bastionkern in " +
                "project bastion-sdk and supply google-services.json before compiling.")
            Spacer(Modifier.height(12.dp))
            Text("The live map is still available in this development build.",
                style = MaterialTheme.typography.bodySmall)
        }
        return
    }

    val auth = remember { FirebaseAuth.getInstance() }
    val repo = remember { FirebaseAccountRepository(auth, FirebaseFirestore.getInstance()) }
    var signedIn by remember { mutableStateOf(auth.currentUser) }
    var feedback by remember { mutableStateOf("") }
    var pending by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var registerMode by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    DisposableEffect(auth) {
        val listener = FirebaseAuth.AuthStateListener { signedIn = it.currentUser }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }

    LaunchedEffect(signedIn?.uid) {
        val uid = signedIn?.uid ?: return@LaunchedEffect
        repo.loadDisplayName(uid) { result ->
            result.onSuccess { name = it }
            result.onFailure { feedback = "Could not load profile: ${it.localizedMessage}" }
        }
    }

    fun done(result: Result<Unit>, success: String) {
        pending = false
        password = ""
        feedback = result.fold(
            onSuccess = { success },
            onFailure = { it.localizedMessage ?: "Authentication request failed" }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text("BASTION-KERN Account", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Firebase project: bastion-sdk", style = MaterialTheme.typography.labelSmall)
        Spacer(Modifier.height(14.dp))

        val user = signedIn
        if (user == null) {
            Text(
                if (registerMode) "Create an account" else "Sign in",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email, onValueChange = { email = it.take(254) },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password, onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !pending && email.contains("@") && password.length >= 6,
                onClick = {
                    pending = true
                    if (registerMode) {
                        repo.register(email, password) { done(it, "Account created. Please verify your email.") }
                    } else {
                        repo.signIn(email, password) { done(it, "Signed in.") }
                    }
                }
            ) {
                Text(if (registerMode) "Create account" else "Sign in")
            }
            TextButton(
                enabled = !pending,
                onClick = {
                    registerMode = !registerMode
                    password = ""
                    feedback = ""
                }
            ) {
                Text(if (registerMode) "Already registered? Sign in" else "New here? Create account")
            }
            if (!registerMode) {
                TextButton(
                    enabled = !pending && email.contains("@"),
                    onClick = {
                        pending = true
                        repo.resetPassword(email) {
                            done(it, "If the address is eligible, a reset email has been sent.")
                        }
                    }
                ) { Text("Reset password") }
            }
        } else {
            Text(
                "Signed in as ${user.email ?: user.uid}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                if (user.isEmailVerified) "Email verified" else "Email verification pending",
                style = MaterialTheme.typography.labelMedium
            )
            if (!user.isEmailVerified) {
                TextButton(onClick = {
                    user.sendEmailVerification().addOnCompleteListener {
                        feedback = if (it.isSuccessful) "Verification email requested."
                        else "Could not send verification email."
                    }
                }) { Text("Resend verification email") }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it.take(60) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Display name") },
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
            Button(
                enabled = !pending,
                onClick = {
                    pending = true
                    repo.updateDisplayName(user.uid, name) { done(it, "Profile saved to Firestore.") }
                }
            ) { Text("Save profile") }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = {
                repo.signOut()
                password = ""
                feedback = "Signed out."
            }) { Text("Sign out") }
        }

        if (pending) {
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
        if (feedback.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(feedback, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(Modifier.height(24.dp))
    }
}
