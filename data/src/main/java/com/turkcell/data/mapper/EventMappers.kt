package com.turkcell.data.mapper

import com.turkcell.data.dto.event.EventDto
import com.turkcell.data.dto.event.TicketTypeDto
import com.turkcell.domain.event.Event
import com.turkcell.domain.ticket.TicketType

internal fun EventDto.toDomain(): Event = Event(
    id=id,
    name=name,
    description=description.orEmpty(),
    venue=venue,
    startsAt=startsAt.orEmpty(),
    endsAt=endsAt.orEmpty(),
    ticketTypes= ticketTypes.map {it.toDomain()}
)

internal fun TicketTypeDto.toDomain() : TicketType = TicketType(
    id=id,
    name=name,
    priceCents=priceCents,
    capacity=capacity,
    soldCount=soldCount,
    remaining=remaining
)