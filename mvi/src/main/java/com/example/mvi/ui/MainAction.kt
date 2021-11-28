package com.example.mvi.ui

sealed class MainAction {
    object Button1Clicked : MainAction()
    object Button2Clicked : MainAction()
    object Jump : MainAction()
}
