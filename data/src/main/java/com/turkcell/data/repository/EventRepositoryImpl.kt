package com.turkcell.data.repository

import com.turkcell.data.remote.EventApi
import com.turkcell.data.util.runCatchingApi
import com.turkcell.domain.EventRepository
import com.turkcell.domain.TicketType
import com.turkcell.domain.Event

class EventRepositoryImpl(
    private val eventApi: EventApi
) : EventRepository {

    override suspend fun getEvents(): Result<List<Event>> =
        runCatchingApi { eventApi.getEvents() }
            .map { dtoList ->
                dtoList.map { dto ->
                    Event(
                        id = dto.id,
                        name = dto.name,
                        description = dto.description,
                        venue = dto.venue,
                        startsAt = dto.startsAt,
                        endsAt = dto.endsAt,
                        ticketTypes = dto.ticketTypes.map { typeDto ->
                            TicketType(
                                id = typeDto.id,
                                name = typeDto.name,
                                priceCents = typeDto.priceCents,
                                capacity = typeDto.capacity,
                                soldCount = typeDto.soldCount,
                                remaining = typeDto.remaining
                            )
                        }
                    )
                }
            }
}