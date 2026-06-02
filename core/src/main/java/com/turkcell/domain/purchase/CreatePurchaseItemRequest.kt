package com.turkcell.domain.purchase


data class CreatePurchaseItemRequest(
    val ticketTypeId: String,
    val quantity: Int
)