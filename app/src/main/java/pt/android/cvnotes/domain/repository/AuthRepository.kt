package pt.android.cvnotes.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.android.cvnotes.data.Result
import pt.android.cvnotes.data.dto.UserDTO

interface AuthRepository {
    fun register(email: String, pwd: String): Flow<Result<UserDTO>>
    fun login(email: String, pwd: String): Flow<Result<UserDTO>>
    fun logout(): Flow<Result<Unit>>
}