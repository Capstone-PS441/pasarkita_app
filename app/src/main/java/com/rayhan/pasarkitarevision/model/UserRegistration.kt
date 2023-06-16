package com.rayhan.pasarkitarevision.model

data class UserRegistrationRequest(
    val username: String,
    val email: String,
    val password: String
)
data class UserLoginRequest(
    val email: String,
    val password: String
)

data class UserData(
    val username: String,
    val email: String
)

data class SignInResponse(
    val id: Int,
    val username: String,
    val email: String,
    val accessToken: String
)

data class RegisterResponse(val message: String)

