package com.ming.trycompose

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
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
//        model.data.observe(this) {
//            when (it) {
//                is Result.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//                is Result.Success -> {
//                    binding.progressBar.visibility = View.INVISIBLE
//                    val randomPage: Int = range.random()
//                    Glide.with(this)
//                        .load(it.data.geeksList[0].bannerList[randomPage].data.cover.feed)
//                        .into(binding.content)
//                    Snackbar.make(binding.content, "第${randomPage}张", Snackbar.LENGTH_SHORT).show()
//                }
//                is Result.Failure -> {
//                    binding.progressBar.visibility = View.INVISIBLE
//                    it.exception.message?.let {
//                        Snackbar.make(binding.content, it, Snackbar.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//            }
//        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.uiState.collect {
                    if (it.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                    val randomPage: Int = range.random()
                    if (it.data.geeksList.isNotEmpty()) {
                        Glide.with(this@MainActivity)
                            .load(it.data.geeksList[0].bannerList[randomPage].data.cover.feed)
                            .into(binding.content)
                    }
                    Snackbar.make(binding.content, "第${randomPage}张", Snackbar.LENGTH_SHORT).show()
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