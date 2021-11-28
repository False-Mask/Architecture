package com.example.mvi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *@author ZhiQiang Tu
 *@time 2021/11/28  10:00
 *@signature 我们不明前路，却已在路上
 */
class MainViewModel2 : ViewModel() {
    fun dispatch(action: MainAction) {
        state.value?.apply {
            _state.value = copy(
                str1 = "1",
                str3 = "3"
            )
        }
        if (state.value == null) {
            _state.value = MainViewState2()
        }
    }

    private val _event: SingleLiveEvent<MainEvent> = SingleLiveEvent()
    val event: LiveData<MainEvent> = _event

    private val _state: MutableLiveData<MainViewState2> = MutableLiveData()
    val state: LiveData<MainViewState2> = _state


}