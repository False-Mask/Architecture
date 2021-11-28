package com.example.mvi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 *@author ZhiQiang Tu
 *@time 2021/11/22  21:15
 *@signature 我们不明前路，却已在路上
 */
class MainViewModel : ViewModel() {

    val event: Channel<MainEvent> = Channel()

    private val _state: MutableStateFlow<MainViewState> = MutableStateFlow(MainViewState())
    val state = _state.asStateFlow()

    fun dispatch(action: MainAction) {
        when (action) {
            is MainAction.Button1Clicked -> button1Clicked()
            is MainAction.Button2Clicked -> button2Clicked()
            is MainAction.Jump-> jump()
        }
    }

    private fun jump() {
        viewModelScope.launch {
            event.send(MainEvent.Jump)
        }
    }


    private fun button2Clicked() {
        /*when (state.value) {
            is MainViewState.Idle -> {
                _state.value = MainViewState.State(
                    tv1 = "Loading",
                    tv2 = "Loading",
                    tv3 = "Loading")
            }

            is MainViewState.State -> {
                _state.value = (state.value as MainViewState.State).copy(
                    tv2 = System.currentTimeMillis().toString()
                )
            }

            is MainViewState.Error -> {

            }
        }*/

        _state.value = state.value.copy(
            tv1= "1"
        )

        viewModelScope.launch {
            event.send(MainEvent.Toast2("Toast2"))
        }
    }

    private fun button1Clicked() {
        _state.value = state.value.copy(
            tv2= "1"
        )
        viewModelScope.launch {
            event.send(MainEvent.Toast1("Toast1"))
        }
    }

}