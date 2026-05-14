package com.turkcell.data.network

// Bağlantı Kopuki timeout, dns çözümleme gibi hatalarda
class NetworkException (cause: Throwable) : RuntimeException("Network Error",cause)

// Sunucu 4xx,5xx hatalar verebilir

class ApiException (
    val code: Int,
    val errorMessage: String?,
    cause: Throwable? = null
) : RuntimeException("HTTP $code: $errorMessage", cause)
