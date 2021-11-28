package com.example.mvi.ext

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlin.reflect.KProperty1

/**
 *@author ZhiQiang Tu
 *@time 2021/11/28  11:40
 *@signature 我们不明前路，却已在路上
 */
fun <A, B> LiveData<A>.observeState(
    lifecycle: LifecycleOwner,
    action: (B) -> Unit,
    property: KProperty1<A, B>,
) {
    this.map {
        property.get(it)
    }
        .distinctUntilChanged()
        .observe(lifecycle, {
            action.invoke(it)
        })
}

fun <A, B> StateFlow<A>.observeState(
    lifecycle: LifecycleOwner,
    property: KProperty1<A, B>,
    action: (B) -> Unit,
){
    this
        .map {
            property.get(it)
        }
        .distinctUntilChanged()
        .onEach {
            action(it)
        }
        .launchIn(lifecycle.lifecycleScope)

}