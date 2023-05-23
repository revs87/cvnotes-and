package pt.android.instacv.data

import kotlinx.coroutines.flow.Flow
import pt.android.instacv.data.dto.UserDTO

interface AuthenticationRepository {
    fun register(email: String, pwd: String): Flow<Result<UserDTO>>
    fun login(email: String, pwd: String): Flow<Result<UserDTO>>
    fun logout(): Flow<Result<Unit>>
}