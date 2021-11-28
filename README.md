# Architecture
This repository contains the popular Android Architecture Demos


# MVI

MVI类似于MVVM，但是不能说他比MVVM更好，只能说它存在自己的一个“特长”在特定的场合下它能使得项目的结构更加清晰。

MVI不是MVVM。

不是，不是，不是！！

他们各有优劣，不能一概而论。



## MVI结构

![MVVM 进阶版：MVI 架构了解一下~](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/395de715244b46459596f14173493ee1~tplv-k3u1fbpfcp-zoom-crop-mark:1304:1304:1304:734.awebp?)



可以从图中看出**MVI更加强调数据的流动，也就是数据流**。

User触发一个Intent然后Intent在model的作用下产生一个ViewState，或者ViewEvent给予view，view刷新。

相比于MVVM看上去**好像变化蛮大的**。然而实际上呢？



请看下图

- MVI

![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2ecd46797c084f08b9efc8fb5246a5db~tplv-k3u1fbpfcp-watermark.awebp)

- MVVM

![img](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8485b8fe71dc44a088832bc63e1abb50~tplv-k3u1fbpfcp-watermark.awebp)



惊喜不？这不是一张图嘛。

所以只能说MVI和MVVM的侧重点不同。

（据说，据说啊MVVM侧重于一个数据的绑定）

![image-20211126204742877](C:\Users\Fool\AppData\Roaming\Typora\typora-user-images\image-20211126204742877.png)

百度百科上的。



而MVI是讲究的数据的一个流动。（MVI是前端的一个架构。）



在我看来MVVM加入了数据的绑定，实现了类似于声明式UI的效果，也就是说View不用关注于UI该怎么呈现，因为View已经和数据绑定在一起了，你数据怎么变我就跟着变（这是侧重点，不是说只有MVVM才能有数据绑定的特性，而是说它相较于MVC，MVP更突出的是这个）



而MVI是讲究的一个数据的流动，由User产生Intent，model映射成一个Event或者一个State发送给View，view在做一个数据的呈现。就这。



MVVM和MVI在大体的结构上是一样的，但是由于侧重点的不同，导致它在一些细节上是不一致的。



## MVI和MVVM的不一致

这是一段MVVM的代码

```kotlin
class TestViewModel : ViewModel() {
    //为保证对外暴露的LiveData不可变，增加一个状态就要添加两个LiveData变量
    private val _pageState: MutableLiveData<PageState> = MutableLiveData()
    val pageState: LiveData<PageState> = _pageState
    private val _state1: MutableLiveData<String> = MutableLiveData()
    val state1: LiveData<String> = _state1
    private val _state2: MutableLiveData<String> = MutableLiveData()
    val state2: LiveData<String> = _state2
    //...
}

```

随着UI界面的数据越来越多MutableLiveData和LiveData会逐渐变多。

然后在View层会有一堆的Observe。



这是MVI

```kotlin
data class MainViewState(val fetchStatus: FetchStatus, val newsList: List<NewsItem>)  

sealed class MainViewEvent {
    data class ShowSnackbar(val message: String) : MainViewEvent()
    data class ShowToast(val message: String) : MainViewEvent()
}
```



```kotlin
class MainViewModel : ViewModel() {
    private val _viewStates: MutableLiveData<MainViewState> = MutableLiveData()
    val viewStates = _viewStates.asLiveData()
    private val _viewEvents: SingleLiveEvent<MainViewEvent> = SingleLiveEvent()
    val viewEvents = _viewEvents.asLiveData()

    init {
        emit(MainViewState(fetchStatus = FetchStatus.NotFetched, newsList = emptyList()))
    }

    private fun fabClicked() {
        count++
        emit(MainViewEvent.ShowToast(message = "Fab clicked count $count"))
    }

    private fun emit(state: MainViewState?) {
        _viewStates.value = state
    }

    private fun emit(event: MainViewEvent?) {
        _viewEvents.value = event
    }
}

```

可以看到无论界面的数据有多少始终只有两个一个ViewState一个ViewEvent。
这样View界面只会有两个Observe，一个观察ViewState的传入，一个观察ViewEvent的传入。因为所有的UI事件和状态加入了一个集中式的管理。



然而正是因为这样不同从而让MVI成为了新的架构，除此之外还带来了一些问题

- 所有的操作都会被映射成一个State，如果界面比较复杂，会导致State的膨胀。你想嘛，一个state里面包含了整个页面需要的数据。这能不复杂嘛。
- state是不可以变的，每次需要跟新ui的状态都会new一个state然后发送给ui层，这导致了一定的内存开销。



## MVI局部刷新

 MVI很大程度上没有受到人们的追捧主要的原因是由于他的数据流。

数据流集中管理UI界面这很好，便以debug，测试，单一可靠数据源，很好。

但是MVI呢他State太过于重量级，当有重复数据发送过来的时候就会造成性能问题。

所以这就得靠局部刷新来解决



- LiveData实现

```kotlin
fun <A,B> LiveData<A>.observeState(
    lifecycle: LifecycleOwner,
    action:(B)->Unit,
    property:KProperty1<A,B>
){
    this.map{
      property.get(it)
    }
      .distinctUntilChanged()
      .observe(lifecycle,{
        action.invoke(it)
      })
}
```

- StateFlow实现

```
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
```



这两个的实现貌似的都不是很完美呢



会发现还是得observe好多次，而且这个好像是很难解决的。

```kotlin
vm.state.observeState(this, { binding.tvView1.text = it }, MainViewState2::str1)
vm.state.observeState(this, { binding.tvView2.text = it }, MainViewState2::str2)
vm.state.observeState(this, { binding.tvView3.text = it }, MainViewState2::str3)
```



```kotlin
vm.state.apply {
    observeState(this@MainActivity, MainViewState::tv1) {
        binding.tvView1.text = it
        Log.e("TAG", "tv1")
    }
    observeState(this@MainActivity, MainViewState::tv2) {
        binding.tvView2.text = it
        Log.e("TAG", "tv2")
    }
    observeState(this@MainActivity, MainViewState::tv3) {
        binding.tvView3.text = it
        Log.e("TAG", "tv3")
    }
}
```

不过至少完成了局部刷新

