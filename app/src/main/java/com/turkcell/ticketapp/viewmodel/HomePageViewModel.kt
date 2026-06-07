package com.turkcell.ticketapp.viewmodel

import androidx.lifecycle.viewModelScope
import com.turkcell.domain.event.Event
import com.turkcell.domain.event.EventRepository
import com.turkcell.domain.ticket.Ticket
import com.turkcell.domain.ticket.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.turkcell.domain.auth.AuthRepository
import androidx.lifecycle.ViewModel


data class HomePageUiState(
    val isEventsLoading: Boolean = false,
    val isEventsRefreshing: Boolean = false,
    val events: List<Event> = emptyList(),
    val eventsError: String? = null,

    val isTicketsLoading: Boolean = false,
    val isTicketsRefreshing: Boolean = false,
    val myTickets: List<Ticket> = emptyList(),
    val ticketsError: String? = null
) {
    val isDashboardRefreshing: Boolean get() = isEventsRefreshing || isTicketsRefreshing
}

class HomePageViewModel(
    private val eventRepository: EventRepository,
    private val ticketRepository: TicketRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomePageUiState())
    val state: StateFlow<HomePageUiState> = _state.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        loadEvents()
        loadMyTickets()
    }

    fun refreshDashboard() {
        refreshEvents()
        refreshMyTickets()
    }

    private fun loadEvents() {
        if (_state.value.isEventsLoading || _state.value.isEventsRefreshing) return
        _state.update { it.copy(isEventsLoading = true, eventsError = null) }
        fetchEvents()
    }

    fun refreshEvents() {
        if (_state.value.isEventsLoading || _state.value.isEventsRefreshing) return
        _state.update { it.copy(isEventsRefreshing = true, eventsError = null) }
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            eventRepository.getEvents()
                .onSuccess { eventList ->
                    _state.update {
                        it.copy(
                            events = eventList,
                            isEventsLoading = false,
                            isEventsRefreshing = false
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            eventsError = error.message ?: "Etkinlikler yüklenemedi",
                            isEventsLoading = false,
                            isEventsRefreshing = false
                        )
                    }
                }
        }
    }

    private fun loadMyTickets() {
        if (_state.value.isTicketsLoading || _state.value.isTicketsRefreshing) return
        _state.update { it.copy(isTicketsLoading = true, ticketsError = null) }
        fetchTickets()
    }

    fun refreshMyTickets() {
        if (_state.value.isTicketsLoading || _state.value.isTicketsRefreshing) return
        _state.update { it.copy(isTicketsRefreshing = true, ticketsError = null) }
        fetchTickets()
    }

    private fun fetchTickets() {
        viewModelScope.launch {
            ticketRepository.getMyTickets()
                .onSuccess { ticketList ->
                    _state.update {
                        it.copy(
                            myTickets = ticketList,
                            isTicketsLoading = false,
                            isTicketsRefreshing = false
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            ticketsError = error.message ?: "Biletler yüklenemedi",
                            isTicketsLoading = false,
                            isTicketsRefreshing = false
                        )
                    }
                }
        }
    }

    fun consumeEventsError() = _state.update { it.copy(eventsError = null) }
    fun consumeTicketsError() = _state.update { it.copy(ticketsError = null) }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _state.value = HomePageUiState()
        }
    }
}