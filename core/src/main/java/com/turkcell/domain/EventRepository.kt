package com.turkcell.domain

import android.media.metrics.Event

interface EventRepository {
    suspend fun getEvents(): Result<List<com.turkcell.domain.Event>>
}