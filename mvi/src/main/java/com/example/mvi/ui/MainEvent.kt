package com.example.mvi.ui

sealed class MainEvent {
    data class Toast1(val content: String) : MainEvent()
    data class Toast2(val content: String) : MainEvent()
    object Jump : MainEvent()
}
