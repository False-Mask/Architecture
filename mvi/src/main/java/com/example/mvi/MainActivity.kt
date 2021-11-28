package com.example.mvi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mvi.databinding.ActivityMainBinding
import com.example.mvi.ext.observeState
import com.example.mvi.ui.MainAction
import com.example.mvi.ui.MainEvent
import com.example.mvi.ui.MainViewModel
import com.example.mvi.ui.MainViewState
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
                when (it) {
                    is MainEvent.Toast1 -> {
                        Toast.makeText(this, it.content, Toast.LENGTH_SHORT).show()
                    }

                    is MainEvent.Toast2 -> {
                        Toast.makeText(this, it.content, Toast.LENGTH_SHORT).show()
                    }

                    is MainEvent.Jump -> {
                        startActivity(Intent(this, MainActivity2::class.java))
                    }
                }
            }
            .launchIn(lifecycleScope)

    }

    private fun initListener() {
        val handler = EventHandler()
        binding.btnIntent1.setOnClickListener(handler)
        binding.btnIntent2.setOnClickListener(handler)
        binding.btnJump.setOnClickListener(handler)
    }

    private fun initStateObserver() {

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
                R.id.btn_jump -> {
                    MainAction.Jump
                }
                else -> {
                    throw Exception("小老弟这个Listener还没设置呢！！")
                }
            }
            vm.dispatch(action)

        }

    }
}
