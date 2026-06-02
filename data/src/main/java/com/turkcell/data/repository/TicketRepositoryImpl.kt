package com.turkcell.data.repository

import com.turkcell.data.remote.TicketApi
import com.turkcell.data.util.runCatchingApi
import com.turkcell.domain.ticket.Ticket
import com.turkcell.domain.ticket.TicketRepository



class TicketRepositoryImpl(
    private val ticketApi: TicketApi
) : TicketRepository {

    override suspend fun getMyTickets(): Result<List<Ticket>> =
        runCatchingApi { ticketApi.getMyTickets() }
            .map { dtoList ->
                dtoList.map { dto ->
                    Ticket(
                        id = dto.id,
                        qrCode = dto.qrCode,
                        status = dto.status,
                        ticketTypeId = dto.ticketTypeId
                    )
                }
            }

    override suspend fun getTicket(id: String): Result<Ticket> =
        runCatchingApi { ticketApi.getTicket(id) }
            .map { dto ->
                Ticket(
                    id = dto.id,
                    qrCode = dto.qrCode,
                    status = dto.status,
                    ticketTypeId = dto.ticketTypeId
                )
            }
}