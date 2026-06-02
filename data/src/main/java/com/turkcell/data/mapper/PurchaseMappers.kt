package com.turkcell.data.mapper

import com.turkcell.domain.purchase.Purchase
import com.turkcell.domain.purchase.PurchaseItem
import com.turkcell.domain.purchase.PurchaseStatus
import com.turkcell.domain.purchase.Ticket
import com.turkcell.domain.purchase.TicketStatus
import com.turkcell.data.dto.purchase.PurchaseItemDto
import com.turkcell.data.dto.purchase.PurchaseResponseDto
import com.turkcell.data.dto.purchase.TicketDto

internal fun PurchaseResponseDto.toDomain(): Purchase = Purchase(
    id = id,
    status = when (status) {
        "PAID" -> PurchaseStatus.PAID
        else -> PurchaseStatus.PENDING
    },
    totalCents = totalCents,
    createdAt = createdAt,
    paidAt = paidAt,
    items = items.map { it.toDomain() },
    tickets = tickets.map { it.toDomain() }
)

internal fun PurchaseItemDto.toDomain(): PurchaseItem = PurchaseItem(
    id = id,
    ticketTypeId = ticketTypeId,
    quantity = quantity,
    unitPriceCents = unitPriceCents
)

internal fun TicketDto.toDomain(): Ticket = Ticket(
    id = id,
    qrCode = qrCode,
    status = when (status) {
        "VALID" -> TicketStatus.VALID
        "USED" -> TicketStatus.USED
        else -> TicketStatus.CANCELLED
    },
    ticketTypeId = ticketTypeId
)