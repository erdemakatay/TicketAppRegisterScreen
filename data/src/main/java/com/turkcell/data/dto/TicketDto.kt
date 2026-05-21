package com.turkcell.data.dto

data class TicketDto(
    val id: String,
    val qrCode: String,
    val status: String,
    val ticketTypeId: String
)
