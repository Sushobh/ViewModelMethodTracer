package com.sushobh.methodlogger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sushobh.methodlogger.ui.theme.DemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel = MainViewModel()
    val launcherViewModel : LauncherViewModel = LauncherViewModel()
    val loginViewModel : LoginViewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                     delay(2000)
                     viewModel.mainRandomMethodName1()
                     viewModel.mainRandomMethodName2()
                     launcherViewModel.launchRandomMethodName1()
                     launcherViewModel.launchRandomMethodName2()
                     loginViewModel.loginRandomMethodName1()
                     loginViewModel.loginRandomMethodName2()
                }
            }
        }

        lifecycleScope.launch {
            setContent {
                DemoTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Column(modifier = Modifier.fillMaxSize() , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally ) {
                            Greeting(
                                name = "Android",
                                modifier = Modifier.padding(innerPadding).clickable {
                                    viewModel.mainRandomMethodName5()
                                    loginViewModel.loginRandomMethodName5()
                                    launcherViewModel.launchRandomMethodName5()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        DemoTheme {
            Greeting("Android")
        }
    }

}