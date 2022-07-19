package com.ming.trycompose.mvi

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ming.trycompose.databinding.ActivityCountBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CountActivity : AppCompatActivity() {

    private val binding: ActivityCountBinding by lazy {
        ActivityCountBinding.inflate(layoutInflater)
    }

    private val viewModel: CountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    Log.e("wtf", "collect:$it")
                    binding.countText.text = it.addCount.toString()
                }
            }
        }

        binding.countAdd.setOnClickListener {
            viewModel.executeAction(CountViewModel.UiAction.Add(2))
        }
        binding.countRemove.setOnClickListener {
            viewModel.executeAction(CountViewModel.UiAction.Remove(1))
        }
    }

}