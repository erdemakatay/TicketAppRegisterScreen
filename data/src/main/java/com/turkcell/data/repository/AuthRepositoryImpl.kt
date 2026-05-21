package com.turkcell.data.repository

import com.turkcell.data.dto.CredentialsDto
import com.turkcell.data.remote.AuthApi
import com.turkcell.data.util.runCatchingApi
import com.turkcell.domain.AuthRepository
import com.turkcell.domain.AuthSession
import com.turkcell.domain.User
import com.turkcell.domain.UserRole
import kotlinx.coroutines.flow.Flow
import com.turkcell.data.local.TokenStore
import kotlinx.coroutines.flow.map


class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore
) : AuthRepository {
    override val isLoggedIn: Flow<Boolean> = tokenStore.accessToken.map { it != null }


    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthSession> = runCatchingApi {
        authApi.login(CredentialsDto(email=email, password=password))
    }.onSuccess {
          tokenStore.save(it.accessToken , it.refreshToken)
    }
        .map {
                i ->
            AuthSession(
                user = User(
                    i.user.id, i.user.email, UserRole.fromApi(i.user.role),
                ),
                accessToken = i.accessToken,
                refreshToken = i.refreshToken

            )
        }


    override suspend fun register(
        email: String,
        password: String
    ): Result<AuthSession> = runCatchingApi {
        authApi.register(CredentialsDto(email = email, password = password))
    }.map { i ->
        AuthSession(
            user = User(
                i.user.id, i.user.email, UserRole.fromApi(i.user.role)
            ),
            accessToken = i.accessToken,
            refreshToken = i.refreshToken
        )
    }

    override suspend fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }
}