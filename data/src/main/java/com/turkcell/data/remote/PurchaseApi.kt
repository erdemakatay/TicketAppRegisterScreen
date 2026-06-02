package com.turkcell.data.remote

import com.turkcell.data.dto.purchase.PurchaseRequestDto
import com.turkcell.data.dto.purchase.PurchaseResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PurchaseApi {
    @POST("/purchases")
    suspend fun createPurchase(@Body request: PurchaseRequestDto): PurchaseResponseDto

    @POST("/purchases/{id}/pay")
    suspend fun pay(@Path("id") id: String): PurchaseResponseDto

    @GET("/purchases/{id}")
    suspend fun getPurchase(@Path("id") id: String): PurchaseResponseDto
}

