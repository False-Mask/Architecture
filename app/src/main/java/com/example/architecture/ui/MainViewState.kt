package com.example.architecture.ui

/**
 *@author ZhiQiang Tu
 *@time 2021/11/22  21:06
 *@signature 我们不明前路，却已在路上
 */
sealed class MainViewState {
    data class State(
        val tv1: String = "",
        val tv2: String = "",
        val tv3: String = "",
    ) : MainViewState()

    object Idle : MainViewState()

    data class Error(
        val throwable: Throwable,
    ) : MainViewState()
}