package com.turkcell.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class TokenPairDto(val user: UserDto, val accessToken:String, val refreshToken: String)