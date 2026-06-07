package com.turkcell.ticketapp.screen

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.ticketapp.viewmodel.TicketDetailViewModel
import org.koin.androidx.compose.koinViewModel
import qrcode.QRCode
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: TicketDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val activity = context as? Activity
        val window = activity?.window
        val layoutParams = window?.attributes

        val originalBrightness = layoutParams?.screenBrightness ?: -1f
        layoutParams?.screenBrightness = 1.0f
        window?.attributes = layoutParams

        onDispose {
            layoutParams?.screenBrightness = originalBrightness
            window?.attributes = layoutParams
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bilet Detayı", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text(state.error!!, color = MaterialTheme.colorScheme.error)
                state.ticket != null -> {
                    val ticket = state.ticket!!

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Bilet #${ticket.id.take(4).uppercase()}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Durum: ${ticket.status.uppercase()}",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (ticket.status.lowercase() == "valid") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )

                        Spacer(Modifier.height(32.dp))

                        val qrBitmap = remember(ticket.qrCode) {
                            generateQrCodeBitmap(ticket.qrCode)
                        }

                        qrBitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "Bilet QR Kodu",
                                modifier = Modifier.size(260.dp)
                            )
                        }

                        Spacer(Modifier.height(32.dp))
                        Text(
                            text = "Girişte bu QR kodu görevliye okutunuz.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

fun generateQrCodeBitmap(payload: String): Bitmap? {
    return try {
        val outputStream = ByteArrayOutputStream()
        QRCode(payload).render().writeImage(outputStream)
        val bytes = outputStream.toByteArray()
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}