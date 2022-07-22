package com.ming.trycompose

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.ming.trycompose.databinding.ActivityMainBinding
import com.ming.trycompose.ui.theme.TryComposeTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val model: GeeksViewModel by viewModels()

    private val range: IntRange = 1..10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContent {
//            TryComposeTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//                    Greeting("Android")
//                }
//            }
//        }
        setContentView(binding.root)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.result.collect {
                        when (it) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                                Snackbar.make(
                                    binding.content,
                                    "Loading...",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                Log.e("wtf", "Loading")
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.INVISIBLE
//                        val randomPage: Int = range.random()
                                if (it.data.geeksList.isNotEmpty()) {
//                                    Glide.with(this@MainActivity)
//                                        .load(it.data.geeksList[0].bannerList[3].data.cover.feed)
//                                        .into(binding.content)
                                }
//                                Snackbar.make(binding.content, "第${3}张", Snackbar.LENGTH_SHORT)
//                                    .show()
                                Log.e("wtf", "Success")
                            }
                            is Result.Failure -> {
                                binding.progressBar.visibility = View.INVISIBLE
//                        Snackbar.make(
//                            binding.content,
//                            "${it.exception.message}",
//                            Snackbar.LENGTH_SHORT
//                        ).show()
                                Log.e("wtf", "${it.exception.message}")
                            }
                        }
                    }
                }
                launch {
                    model.message.collect {
                        Snackbar.make(
                            binding.content,
                            it,
                            Snackbar.LENGTH_SHORT
                        ).show()
                        Glide.with(this@MainActivity)
                            .load(it)
                            .into(binding.content)
                    }
                }
            }

        }

        binding.post.setOnClickListener {
            model.load(System.currentTimeMillis(), 1)
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TryComposeTheme {
        Greeting("Android")
    }
}