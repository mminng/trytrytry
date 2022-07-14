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
import com.google.android.material.snackbar.Snackbar
import com.ming.trycompose.databinding.ActivityMainBinding
import com.ming.trycompose.ui.theme.TryComposeTheme

class MainActivity : ComponentActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val model: GeeksViewModel by viewModels()

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
                var errorMessage = ""
                response.showError?.let {
                    errorMessage = it
                }
                if (errorMessage.isEmpty()) {
                    response.geeksList?.let {
                        binding.message.text = it[0].bannerList[1].data.cover.feed
                    }
                } else {
                    binding.message.text = errorMessage
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