package com.turkcell.ticketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.domain.ticket.Ticket
import com.turkcell.domain.ticket.TicketRepository
import com.turkcell.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyTicketsUiState(
    val isLoading: Boolean = false,
    val tickets: List<Ticket> = emptyList(),
    val error: String? = null
)

class MyTicketsViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MyTicketsUiState())
    val state: StateFlow<MyTicketsUiState> = _state.asStateFlow()

    init {
        loadTickets()
    }

    fun loadTickets() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            ticketRepository.getMyTickets().fold(
                onSuccess = { ticketList ->
                    _state.update { it.copy(isLoading = false, tickets = ticketList) }
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.toUserMessage()
                        )
                    }
                }
            )
        }
    }

    fun consumeError() = _state.update { it.copy(error = null) }
}