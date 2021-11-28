package com.example.mvi

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mvi.databinding.ActivityMain2Binding
import com.example.mvi.ext.observeState
import com.example.mvi.ui.MainAction
import com.example.mvi.ui.MainViewModel2
import com.example.mvi.ui.MainViewState2

class MainActivity2 : AppCompatActivity() {

    private val vm: MainViewModel2 by viewModels()

    private val binding: ActivityMain2Binding by lazy {
        ActivityMain2Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initListener()
        initObserver()
    }

    private fun initObserver() {
        vm.state.observeState(this, { binding.tvView1.text = it }, MainViewState2::str1)
        vm.state.observeState(this, { binding.tvView2.text = it }, MainViewState2::str2)
        vm.state.observeState(this, { binding.tvView3.text = it }, MainViewState2::str3)
    }

    private fun initListener() {
        val handler = EventHandler()
        binding.btnIntent1.setOnClickListener(handler)
        binding.btnIntent2.setOnClickListener(handler)
    }


    inner class EventHandler : View.OnClickListener {
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