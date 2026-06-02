package com.turkcell.ticketapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.domain.ticket.Ticket
import com.turkcell.ticketapp.R 
import com.turkcell.ticketapp.viewmodel.HomePageViewModel
import com.turkcell.domain.event.Event
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageScreen(
    viewModel: HomePageViewModel = koinViewModel(),
    onNavigateToEventDetail: (String) -> Unit = {},
    onNavigateToTicketDetail: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val navigationTabs = listOf(
        stringResource(id = R.string.tab_label_events),
        stringResource(id = R.string.tab_label_tickets)
    )

    state.errorMessage?.let { LaunchedEffect(it) { viewModel.consumeError() } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.tab_label_events), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                actions = {
                    IconButton(onClick = { viewModel.logout() }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, stringResource(id = R.string.action_sign_out))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                navigationTabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            if (state.errorMessage != null) {
                Text(text = state.errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when (selectedTabIndex) {
                    0 -> when {
                        state.isLoadingEvents -> CircularProgressIndicator()
                        state.events.isEmpty() -> Text(stringResource(id = R.string.status_no_events), style = MaterialTheme.typography.bodyLarge)
                        else -> LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(state.events) { currentEvent ->
                                EventItem(event = currentEvent, onEventClick = onNavigateToEventDetail)
                            }
                        }
                    }
                    1 -> when {
                        state.isLoadingTickets -> CircularProgressIndicator()
                        state.myTickets.isEmpty() -> Text(stringResource(id = R.string.status_no_tickets), style = MaterialTheme.typography.bodyLarge)
                        else -> LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(state.myTickets) { currentTicket ->
                                TicketItem(ticket = currentTicket, onTicketClick = onNavigateToTicketDetail)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventItem(event: Event, onEventClick: (String) -> Unit) {
    Card(
        onClick = { onEventClick(event.id) },
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "📍 ${event.venue}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "🕐 ${event.startsAt.take(10)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (event.ticketTypes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(6.dp))
                event.ticketTypes.forEach { type ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = type.name, style = MaterialTheme.typography.bodySmall)
                        Text(text = "${type.priceCents / 100} ₺", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun TicketItem(ticket: Ticket, onTicketClick: (String) -> Unit) {
    val (badgeColor, badgeText) = when (ticket.status.uppercase()) {
        "VALID" -> Color(0xFF2E7D32) to stringResource(id = R.string.state_active)
        "USED" -> Color(0xFF757575) to stringResource(id = R.string.state_finished)
        else -> Color(0xFFC62828) to ticket.status
    }

    Card(
        onClick = { onTicketClick(ticket.id) },
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Bilet #${ticket.id.take(8).uppercase()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${stringResource(id = R.string.card_label_category)} ${ticket.ticketTypeId}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Payload: ${ticket.qrCode.take(8)}...",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Surface(
                color = badgeColor,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = badgeText,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}