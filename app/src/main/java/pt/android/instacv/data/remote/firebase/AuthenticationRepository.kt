package pt.android.instacv.data.remote.firebase

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import pt.android.instacv.data.AuthenticationRepository
import pt.android.instacv.data.Result
import pt.android.instacv.data.dto.UserDTO
import pt.android.instacv.data.local.SPKey
import pt.android.instacv.data.local.SharedPreferencesRepository

internal class FirebaseAuthRepositoryImpl(
    private val spRepository: SharedPreferencesRepository
) : AuthenticationRepository {
    private val mAuth = FirebaseAuth.getInstance()

    override fun register(email: String, pwd: String): Flow<Result<UserDTO>> = flow {
        try {
            val res = mAuth.createUserWithEmailAndPassword(email, pwd).await()
            if (res.isValid()) {
                cacheUser(res)
                emit(Result.Success(res.toUserDTO()))
            }
            else { emit(Result.Error("Registered user is not valid")) }
        }
        catch (e: Exception) { emit(Result.Error("Creating user failed")) }
    }

    override fun login(email: String, pwd: String): Flow<Result<UserDTO>> = flow {
        try {
            val res = mAuth.signInWithEmailAndPassword(email, pwd).await()
            if (res.isValid()) {
                cacheUser(res)
                emit(Result.Success(res.toUserDTO()))
            }
            else { emit(Result.Error("User is not valid")) }
        }
        catch (e: Exception) { emit(Result.Error("Signing in failed")) }
    }

    override fun logout() = flow {
        try {
            mAuth.signOut()
            spRepository.purgeAll()
            emit(Result.Success(Unit))
        }
        catch (e: Exception) { emit(Result.Error("Signing out failed")) }
    }

    private fun cacheUser(res: AuthResult) {
        res.user?.let {
            spRepository.putString(SPKey.UID.key, it.uid)
            spRepository.putString(SPKey.EMAIL.key, it.email.orEmpty())
            spRepository.putString(SPKey.DISPLAY_NAME.key, it.displayName.orEmpty())
            spRepository.putString(SPKey.PHONE_NUMBER.key, it.phoneNumber.orEmpty())
            spRepository.putString(SPKey.PHOTO_URL.key, it.photoUrl?.path.orEmpty())
        }
        res.additionalUserInfo?.let {
            spRepository.putString(SPKey.USERNAME.key, it.username.orEmpty())
        }
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
