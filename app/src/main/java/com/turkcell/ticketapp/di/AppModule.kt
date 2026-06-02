package com.turkcell.ticketapp.di

import com.turkcell.ticketapp.viewmodel.MyTicketsViewModel
import com.turkcell.ticketapp.viewmodel.EventDetailViewModel
import com.turkcell.ticketapp.viewmodel.HomePageViewModel
import com.turkcell.ticketapp.viewmodel.LoginViewModel
import com.turkcell.ticketapp.viewmodel.RegisterViewModel
import com.turkcell.ticketapp.viewmodel.TicketDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomePageViewModel(get(), get(),get()) }
    viewModel { EventDetailViewModel(get(), get(), get()) }
    viewModel { MyTicketsViewModel(get()) }
    viewModel { TicketDetailViewModel(get(), get()) }

}