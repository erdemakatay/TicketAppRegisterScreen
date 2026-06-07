package com.turkcell.domain.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedIn: Flow<Boolean?>
    val currentUser: Flow<User?>

    suspend fun login(email: String, password: String): Result<AuthSession>
    suspend fun register(email: String, password: String): Result<AuthSession>
    suspend fun logout(): Result<Unit>
}
