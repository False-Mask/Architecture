package com.example.architecture.ui

sealed class MainAction {
    object Button1Clicked : MainAction()
    object Button2Clicked : MainAction()
}
