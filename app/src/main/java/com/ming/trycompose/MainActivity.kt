package com.ming.trycompose

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.ming.trycompose.databinding.ActivityMainBinding
import com.ming.trycompose.ui.theme.TryComposeTheme

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
        model.data.observe(this) { response ->
            if (response.showProgress) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = response.showError
                if (errorMessage.isEmpty()) {
                    val randomPage: Int = range.random()
                    Glide.with(this)
                        .load(response.geeksList[0].bannerList[randomPage].data.cover.feed)
                        .into(binding.content)
                    Snackbar.make(binding.content, "第${randomPage}张", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.content, errorMessage, Snackbar.LENGTH_SHORT).show()
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