package pt.android.instacv.data.remote.firebase

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pt.android.instacv.data.Result
import pt.android.instacv.data.dto.UserDTO

interface AuthenticationRepository {
    fun register(email: String, pwd: String): Flow<Result<UserDTO>>
    fun login(email: String, pwd: String): Flow<Result<UserDTO>>
    fun logout(): Flow<Result<Unit>>
}

internal class AuthenticationRepositoryImpl : AuthenticationRepository {
    private val mAuth = FirebaseAuth.getInstance()

    override fun register(email: String, pwd: String): Flow<Result<UserDTO>> = flow {
        try {
            val res = mAuth.createUserWithEmailAndPassword(email, pwd).result
            if (!res.isValid()) { emit(Result.Error("Registered user is not valid")) }
            else { emit(Result.Success(res.toUserDTO())) }
        }
        catch (e: Exception) { emit(Result.Error("Creating user failed")) }
    }

    override fun login(email: String, pwd: String): Flow<Result<UserDTO>> = flow {
        try {
            val res = mAuth.signInWithEmailAndPassword(email, pwd).result
            if (!res.isValid()) { emit(Result.Error("User is not valid")) }
            else { emit(Result.Success(res.toUserDTO())) }
        }
        catch (e: Exception) { emit(Result.Error("Signing in failed")) }
    }

    override fun logout() = flow {
        try {
            mAuth.signOut()
            emit(Result.Success(Unit))
        }
        catch (e: Exception) { emit(Result.Error("Signing out failed")) }
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

