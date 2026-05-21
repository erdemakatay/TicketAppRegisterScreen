package com.turkcell.domain

interface TicketRepository {
    suspend fun getMyTickets(): Result<List<Ticket>>
}