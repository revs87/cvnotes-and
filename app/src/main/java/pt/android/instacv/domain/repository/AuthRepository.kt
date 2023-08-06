package pt.android.instacv.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.android.instacv.data.Result
import pt.android.instacv.data.dto.UserDTO

interface AuthRepository {
    fun register(email: String, pwd: String): Flow<Result<UserDTO>>
    fun login(email: String, pwd: String): Flow<Result<UserDTO>>
    fun logout(): Flow<Result<Unit>>
}