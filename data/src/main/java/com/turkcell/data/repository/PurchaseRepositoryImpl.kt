package com.turkcell.data.repository


import com.turkcell.domain.purchase.CreatePurchaseItemRequest
import com.turkcell.domain.purchase.Purchase
import com.turkcell.domain.purchase.PurchaseRepository
import com.turkcell.data.dto.purchase.PurchaseItemRequestDto
import com.turkcell.data.dto.purchase.PurchaseRequestDto
import com.turkcell.data.mapper.toDomain
import com.turkcell.data.remote.PurchaseApi
import com.turkcell.data.util.runCatchingApi

class PurchaseRepositoryImpl(
    private val purchaseApi: PurchaseApi
) : PurchaseRepository {

    override suspend fun createPurchase(items: List<CreatePurchaseItemRequest>): Result<Purchase> =
        runCatchingApi {
            purchaseApi.createPurchase(
                PurchaseRequestDto(
                    items = items.map {
                        PurchaseItemRequestDto(
                            ticketTypeId = it.ticketTypeId,
                            quantity = it.quantity
                        )
                    }
                )
            )
        }.map { it.toDomain() }

    override suspend fun pay(purchaseId: String): Result<Purchase> =
        runCatchingApi { purchaseApi.pay(purchaseId) }.map { it.toDomain() }

    override suspend fun getPurchase(purchaseId: String): Result<Purchase> =
        runCatchingApi { purchaseApi.getPurchase(purchaseId) }.map { it.toDomain() }
}