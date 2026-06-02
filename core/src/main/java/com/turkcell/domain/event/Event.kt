package com.turkcell.domain.event

import com.turkcell.domain.ticket.TicketType

data class Event(
    val id: String,
    val name: String,
    val description: String,
    val venue: String,
    val startsAt: String,
    val endsAt: String,
    val ticketTypes: List<TicketType>
)
