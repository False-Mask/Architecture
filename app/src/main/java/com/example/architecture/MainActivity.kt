package com.example.architecture

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.architecture.databinding.ActivityMainBinding
import com.example.architecture.ui.MainAction
import com.example.architecture.ui.MainEvent
import com.example.architecture.ui.MainViewModel
import com.example.architecture.ui.MainViewState
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initListener()
        initStateObserver()
        initEventObserver()
    }

    private fun initEventObserver() {

        vm.event
            .consumeAsFlow()
            .onEach {
                when(it){
                    is MainEvent.Toast1->{
                        Toast.makeText(this, it.content, Toast.LENGTH_SHORT).show()
                    }

                    is MainEvent.Toast2->{
                        Toast.makeText(this, it.content, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .launchIn(lifecycleScope)

    }

    private fun initListener() {
        val handler = EventHandler()
        binding.btnIntent1.setOnClickListener(handler)
        binding.btnIntent2.setOnClickListener(handler)
    }

    private fun initStateObserver() {

        vm.state
            .onEach {
                when(it){
                    is MainViewState.Idle->{
                        binding.tvView1.text = "Idle"
                        binding.tvView2.text = "Idle"
                        binding.tvView3.text = "Idle"
                    }

                    is MainViewState.State->{
                        binding.tvView1.text = it.tv1
                        binding.tvView2.text = it.tv2
                        binding.tvView3.text = it.tv3
                    }

                    is MainViewState.Error->{
                        binding.tvView1.text = "Error"
                        binding.tvView2.text = "Error"
                        binding.tvView3.text = "Error"
                    }

                }
            }
            .launchIn(lifecycleScope)

    }

    inner class EventHandler() : View.OnClickListener {
        override fun onClick(v: View?) {
            v ?: return
            val action: MainAction = when (v.id) {
                R.id.btn_intent1 -> {
                    MainAction.Button1Clicked
                }
                R.id.btn_intent2 -> {
                    MainAction.Button2Clicked
                }
                else -> {
                    throw Exception("小老弟这个Listener还没设置呢！！")
                }
            }

            vm.dispatch(action)

        }

    }
}