package com.turkcell.data.dto

data class EventDto(
    val id: String,
    val name: String,
    val description: String,
    val venue: String,
    val startsAt: String,
    val endsAt: String,
    val ticketTypes: List<TicketTypeDto>
)

data class TicketTypeDto(
    val id: String,
    val name: String,
    val priceCents: Int,
    val capacity: Int,
    val soldCount: Int,
    val remaining: Int
)