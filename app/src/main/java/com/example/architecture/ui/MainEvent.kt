package com.example.architecture.ui

sealed class MainEvent {
    data class Toast1(val content: String) : MainEvent()
    data class Toast2(val content: String) : MainEvent()
}
