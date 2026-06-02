package com.turkcell.domain.auth

import com.turkcell.domain.auth.User

data class AuthSession(val user: User, val accessToken: String, val refreshToken: String) {}