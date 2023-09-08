package pt.rvcoding.cvnotes.data.dto

import kotlin.random.Random

data class UserDTO(
    val uid: String = Random.nextLong(0, Long.MAX_VALUE).toString(),
    val email: String,
    val userName: String = "",
    val displayName: String = "",
    val phoneNumber: String = "",
    val photoUrl: String = "",
)