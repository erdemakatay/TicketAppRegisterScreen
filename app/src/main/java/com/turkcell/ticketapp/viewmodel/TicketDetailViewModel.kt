package com.turkcell.ticketapp.viewmodel

import androidx.lifecycle.SavedStateHandle
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

data class TicketDetailUiState(
    val isLoading: Boolean = false,
    val ticket: Ticket? = null,
    val error: String? = null
)

class TicketDetailViewModel(
    private val ticketRepository: TicketRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val ticketId: String = checkNotNull(savedStateHandle["ticketId"])

    private val _state = MutableStateFlow(TicketDetailUiState())
    val state: StateFlow<TicketDetailUiState> = _state.asStateFlow()

    init {
        loadTicketDetail()
    }

    fun loadTicketDetail() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            ticketRepository.getTicket(ticketId).fold(
                onSuccess = { ticketDetail ->
                    _state.update { it.copy(isLoading = false, ticket = ticketDetail) }
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
}