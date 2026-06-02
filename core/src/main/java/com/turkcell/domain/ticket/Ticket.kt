package com.turkcell.domain.ticket

data class Ticket(
    val id: String,
    val qrCode: String,
    val status: String,
    val ticketTypeId: String
)