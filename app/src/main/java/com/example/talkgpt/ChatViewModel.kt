package com.example.talkgpt

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

data class MessageModel(val message: String, val sender: String)

class ChatViewModel : ViewModel() {
    val messageList = mutableStateListOf<MessageModel>()

    val generativeModel = GenerativeModel(
        // Ensure the correct initialization with proper modelName and apiKey
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    fun sendMessage(message: String) {
        viewModelScope.launch {
            // Add user message to the message list
            messageList.add(MessageModel(message, "user"))

            // Initialize a chat session with the existing chat history
            val chat = generativeModel.startChat(
                history = messageList.map {
                    content(it.sender) { text(it.message) }
                }.toList()
            )

            // Send user message to the chat session and get the response
            val response = chat.sendMessage(message)

            // Add bot response to the message list
            messageList.add(MessageModel(response.text.toString(), "model"))


        }
    }
}

