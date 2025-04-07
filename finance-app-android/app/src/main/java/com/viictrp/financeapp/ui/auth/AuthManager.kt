package com.viictrp.financeapp.ui.auth

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.viictrp.financeapp.application.dto.UserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthManager(private val context: Context) {

    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val credentialManager = CredentialManager.create(context)

    var user: UserDTO?
        get() {
            val email = prefs.getString("user_email", null)
            val fullName = prefs.getString("user_fullname", null)
            val pictureUrl = prefs.getString("user_profile_picture_url", null)
            val accessToken = prefs.getString("access_token", null)

            return if (email != null || fullName != null || pictureUrl != null || accessToken != null) {
                UserDTO(
                    email = email ?: "",
                    fullName = fullName ?: "",
                    pictureUrl = pictureUrl ?: "",
                    accessToken = accessToken ?: ""
                )
            } else {
                null
            }
        }
        set(user) {
            prefs.edit {
                putString("user_email", user?.email)
                putString("user_fullname", user?.fullName)
                putString("user_profile_picture_url", user?.pictureUrl)
                putString("access_token", user?.accessToken)
            }
        }

    fun clearTokens() {
        prefs.edit { clear() }
    }

    fun isLoggedIn(): Boolean = !user?.accessToken.isNullOrBlank()

    suspend fun signInWithGoogle(onResult: (user: UserDTO?, message: String?) -> Unit) =
        withContext(Dispatchers.IO) {
            try {
                val googleCredential = getGoogleCredential() ?: run {
                    withContext(Dispatchers.Main) {
                        onResult(null, "Credencial não é GoogleIdTokenCredential")
                    }
                    return@withContext
                }

                val firebaseCredential =
                    GoogleAuthProvider.getCredential(googleCredential.idToken, null)

                FirebaseAuth.getInstance()
                    .signInWithCredential(firebaseCredential)
                    .addOnSuccessListener { result ->
                        result.user?.getIdToken(true)?.addOnSuccessListener { tokenResult ->
                            val user = createUserFromFirebase(result.user, tokenResult.token)
                            this@AuthManager.user = user
                            onResult(user, null)
                        }
                    }
                    .addOnFailureListener {
                        onResult(null, "Autenticação Firebase falhou")
                    }

            } catch (e: Exception) {
                Log.e("AuthManager", "Login failed: ${e.message}")
                withContext(Dispatchers.Main) {
                    onResult(null, e.message)
                }
            }
        }

    private suspend fun getGoogleCredential(): GoogleIdTokenCredential? {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("13736117814-3v0d8tvlff21kavkvat58ol2n372g05h.apps.googleusercontent.com")
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val response: GetCredentialResponse = credentialManager.getCredential(context, request)
        val credential = response.credential

        return if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            GoogleIdTokenCredential.createFrom(credential.data)
        } else {
            null
        }
    }

    private fun createUserFromFirebase(user: FirebaseUser?, token: String?): UserDTO {
        return UserDTO(
            email = user?.email ?: "",
            fullName = user?.displayName ?: "",
            pictureUrl = user?.photoUrl?.toString() ?: "",
            accessToken = token ?: ""
        )
    }
}