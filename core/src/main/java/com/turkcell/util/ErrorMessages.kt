package com.turkcell.util


class NetworkException(cause: Throwable) : RuntimeException("Network Error", cause)

class ApiException(
    val code: Int,
    val errorMessage: String?,
    cause: Throwable? = null
) : RuntimeException("HTTP $code: $errorMessage", cause)


fun Throwable.toUserMessage(
    extraCodes: Map<Int, String> = emptyMap()
): String {
    return when (this) {
        is ApiException -> {
            val currentCode = this.code
            val messageText = this.errorMessage ?: ""

            extraCodes[currentCode] ?: when (currentCode) {
                401 -> "Email veya şifre hatalı."

                403 -> when (messageText) {
                    "not_purchase_owner" -> "Bu satın alım size ait değil."
                    else -> "Bu işlemi yapmaya yetkiniz yok."
                }

                404 -> "İstenen kaynak bulunamadı."

                409 -> when (messageText) {
                    "capacity_exceeded" -> "Stok yetersiz, lütfen etkinlik sayfasını yenileyin."
                    "already_paid" -> "Bu satın alım zaten ödenmiş."
                    else -> "Bir çakışma hatası oluştu."
                }

                in 500..599 -> "Sunucu şu anda cevap veremiyor."
                else -> "Beklenmeyen bir hata oluştu."
            }
        }
        is NetworkException -> "İnternet bağlantısı yok."
        else -> this.message ?: "Bilinmeyen bir hata oluştu."
    }
}