package pt.rvcoding.cvnotes.data.repository.firebase

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import pt.rvcoding.cvnotes.data.Result
import pt.rvcoding.cvnotes.data.Result.Error
import pt.rvcoding.cvnotes.data.Result.Success
import pt.rvcoding.cvnotes.data.SPKey
import pt.rvcoding.cvnotes.data.dto.UserDTO
import pt.rvcoding.cvnotes.domain.repository.AuthRepository
import pt.rvcoding.cvnotes.domain.repository.SharedPreferencesRepository
import pt.rvcoding.cvnotes.domain.util.sha256

internal class FirebaseAuthRepositoryImpl(
    private val spRepository: SharedPreferencesRepository
) : AuthRepository {
    private val mAuth = FirebaseAuth.getInstance()

    override fun register(email: String, pwd: String, isOffline: Boolean): Flow<Result<UserDTO>> = flow {
        if (isOffline) {
            val offlineUser = UserDTO(email = email)
            cacheOfflineUser(offlineUser)
            emit(Success(offlineUser))
        }
        else {
            try {
                val safePwd = pwd.sha256()
                val res = mAuth.createUserWithEmailAndPassword(email, safePwd).await()
                if (res.isValid()) {
                    cacheUser(res)
                    emit(Success(res.toUserDTO()))
                }
                else { emit(Error("Registered user is not valid")) }
            }
            catch (e: Exception) { emit(Error("Creating user failed: ${e.message}")) }
        }
    }

    override fun login(email: String, pwd: String): Flow<Result<UserDTO>> = flow {
        try {
            val safePwd = pwd.sha256()
            val res = mAuth.signInWithEmailAndPassword(email, safePwd).await()
            if (res.isValid()) {
                cacheUser(res)
                emit(Success(res.toUserDTO()))
            }
            else { emit(Error("User is not valid")) }
        }
        catch (e: Exception) { emit(Error("Signing in failed: ${e.message}")) }
    }

    override fun logout() = flow {
        try {
            mAuth.signOut()
            spRepository.purgeAll()
            emit(Success(Unit))
        }
        catch (e: Exception) { emit(Error("Signing out failed")) }
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

    private fun cacheOfflineUser(offlineUser: UserDTO) {
        spRepository.putString(SPKey.UID.key, offlineUser.uid)
        spRepository.putString(SPKey.EMAIL.key, offlineUser.email)
        spRepository.putString(SPKey.DISPLAY_NAME.key, "Offline account")
        spRepository.putString(SPKey.PHONE_NUMBER.key, "-")
        spRepository.putString(SPKey.PHOTO_URL.key, "-")
        spRepository.putString(SPKey.USERNAME.key, offlineUser.email)
    }
}
