package com.turkcell.data.repository

import com.turkcell.data.dto.auth.CredentialsDto
import com.turkcell.data.remote.AuthApi
import com.turkcell.data.util.runCatchingApi
import com.turkcell.domain.auth.AuthRepository
import com.turkcell.domain.auth.AuthSession
import com.turkcell.domain.auth.User
import com.turkcell.domain.auth.UserRole
import kotlinx.coroutines.flow.Flow
import com.turkcell.data.local.TokenStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore,
    private val scope: CoroutineScope = GlobalScope
) : AuthRepository {

    override val isLoggedIn: Flow<Boolean?> = tokenStore.accessToken
        .map { token ->
            if (token == null) false
            else token.isNotEmpty()
        }        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    override val currentUser: Flow<User?> = tokenStore.userRole.map { role ->
        role?.let { User(id = "", email = "", role = UserRole.fromApi(it)) }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthSession> = runCatchingApi {
        authApi.login(CredentialsDto(email = email, password = password))
    }.onSuccess {
        tokenStore.save(it.accessToken, it.refreshToken, it.user.role)
    }.map { i ->
        val user = User(i.user.id, i.user.email, UserRole.fromApi(i.user.role))
        AuthSession(user = user, accessToken = i.accessToken, refreshToken = i.refreshToken)
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<AuthSession> = runCatchingApi {
        authApi.register(CredentialsDto(email = email, password = password))
    }.onSuccess {
        tokenStore.save(it.accessToken, it.refreshToken, it.user.role)
    }.map { i ->
        val user = User(i.user.id, i.user.email, UserRole.fromApi(i.user.role))
        AuthSession(user = user, accessToken = i.accessToken, refreshToken = i.refreshToken)
    }

    override suspend fun logout(): Result<Unit> {
        tokenStore.clear()
        return Result.success(Unit)
    }
}