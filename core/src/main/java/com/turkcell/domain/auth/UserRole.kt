package com.turkcell.domain.auth

enum class UserRole {
    USER, STAFF, ADMIN;

    companion object {
        // parser func. (çevirici fonksiyon backkendden gelen cevabı enuma çevirme yapısı.)
        fun fromApi(value: String?): UserRole = when (value?.uppercase()) {
            "ADMIN" -> UserRole.ADMIN
            "STAFF" -> UserRole.STAFF
            else -> UserRole.USER
        }
    }
}