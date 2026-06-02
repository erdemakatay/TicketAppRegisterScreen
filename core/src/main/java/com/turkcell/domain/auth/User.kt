package com.turkcell.domain.auth

import com.turkcell.domain.auth.UserRole

data class User(val id: String, val email:String, val role: UserRole) {}