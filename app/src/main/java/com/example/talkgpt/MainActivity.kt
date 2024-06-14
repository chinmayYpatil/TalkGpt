package com.example.talkgpt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.talkgpt.ui.theme.TalkGPTTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        setContent {
            TalkGPTTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TalkGPT(modifier = Modifier.padding(innerPadding), chatViewModel)
                }
            }
        }
    }
}
