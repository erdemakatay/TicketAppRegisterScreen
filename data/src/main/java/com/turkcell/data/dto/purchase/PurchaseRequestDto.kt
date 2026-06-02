package com.turkcell.data.dto.purchase

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseRequestDto(
    val items: List<PurchaseItemRequestDto>
)

@Serializable
data class PurchaseItemRequestDto(
    val ticketTypeId: String,
    val quantity: Int
)