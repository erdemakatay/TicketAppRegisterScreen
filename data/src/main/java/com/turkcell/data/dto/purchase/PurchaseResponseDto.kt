package com.turkcell.data.dto.purchase

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseResponseDto(
    val id: String,
    val status: String,
    val totalCents: Long,
    val createdAt: String,
    val paidAt: String? = null,
    val items: List<PurchaseItemDto>,
    val tickets: List<TicketDto> = emptyList()
)

@Serializable
data class PurchaseItemDto(
    val id: String,
    val ticketTypeId: String,
    val quantity: Int,
    val unitPriceCents: Long
)

@Serializable
data class TicketDto(
    val id: String,
    val qrCode: String,
    val status: String,
    val ticketTypeId: String
)