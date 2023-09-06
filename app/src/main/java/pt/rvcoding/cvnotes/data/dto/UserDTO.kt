package pt.rvcoding.cvnotes.data.dto

data class UserDTO(
    val uid: String,
    val email: String,
    val userName: String,
    val displayName: String,
    val phoneNumber: String,
    val photoUrl: String,
)