package com.turkcell.domain.ticket

interface TicketRepository {
    suspend fun getMyTickets(): Result<List<Ticket>>
    suspend fun getTicket(id: String): Result<Ticket>
}