package pt.android.instacv.data.remote.firebase

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import pt.android.instacv.data.ApiResult
import pt.android.instacv.data.dto.UserDTO
import javax.inject.Inject

interface FirebaseDataRepository {
    suspend fun register(email: String, pwd: String): ApiResult<UserDTO>
    suspend fun login(email: String, pwd: String): ApiResult<UserDTO>
    suspend fun logout(): ApiResult<Unit>
}

internal class FirebaseDataRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher
) : FirebaseDataRepository {
    private val mAuth = FirebaseAuth.getInstance()

    override suspend fun register(email: String, pwd: String): ApiResult<UserDTO> = withContext(dispatcher) {
        try {
            val res = mAuth.createUserWithEmailAndPassword(email, pwd).result
            if (!res.isValid()) { ApiResult.Error("User is not valid") }
            else { ApiResult.Success(res.toUserDTO()) }
        } catch (e: Exception) { ApiResult.Error("Signing in failed") }
    }

    override suspend fun login(email: String, pwd: String): ApiResult<UserDTO> = withContext(dispatcher) {
        try {
            val res = mAuth.signInWithEmailAndPassword(email, pwd).result
            if (!res.isValid()) { ApiResult.Error("User is not valid") }
            else { ApiResult.Success(res.toUserDTO()) }
        } catch (e: Exception) { ApiResult.Error("Signing in failed") }
    }

    override suspend fun logout() = withContext(dispatcher) {
        try {
            mAuth.signOut()
            ApiResult.Success(Unit)
        } catch (e: Exception) { ApiResult.Error("Signing out failed") }
    }

    private fun AuthResult?.toUserDTO(): UserDTO = UserDTO(
        this?.user?.uid.orEmpty(),
        this?.user?.email.orEmpty(),
        this?.additionalUserInfo?.username.orEmpty(),
        this?.user?.displayName.orEmpty(),
        this?.user?.phoneNumber.orEmpty(),
        this?.user?.photoUrl?.path.orEmpty(),
    )

    private fun AuthResult?.isValid(): Boolean = when {
        this == null -> false
        this.user == null -> false
        this.additionalUserInfo == null -> false
        this.user?.uid.isNullOrBlank() -> false
        this.user?.email.isNullOrBlank() -> false
        else -> true
    }
}

