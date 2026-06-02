package com.turkcell.data.dto.auth

data class TicketDto(
    val id: String,
    val qrCode: String,
    val status: String,
    val ticketTypeId: String
)