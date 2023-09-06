package pt.rvcoding.cvnotes.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.rvcoding.cvnotes.data.Result
import pt.rvcoding.cvnotes.data.dto.UserDTO

interface AuthRepository {
    fun register(email: String, pwd: String): Flow<Result<UserDTO>>
    fun login(email: String, pwd: String): Flow<Result<UserDTO>>
    fun logout(): Flow<Result<Unit>>
}