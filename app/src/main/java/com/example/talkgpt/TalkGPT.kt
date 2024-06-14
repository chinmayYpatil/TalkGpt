package com.example.talkgpt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TalkGPT(modifier: Modifier = Modifier, chatViewModel: ChatViewModel) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader()
        Spacer(modifier = Modifier.height(16.dp))
        val isMessageEmpty = chatViewModel.messageList.isEmpty()
        messageList(
            modifier = Modifier.weight(1f),
            messageList = chatViewModel.messageList,
            userLogo = painterResource(id = R.drawable.user_logo),
            talkGptLogo = painterResource(id = R.drawable.talk_gpt_logo),
            isMessageEmpty = isMessageEmpty
        )
        Spacer(modifier = Modifier.height(16.dp))
        val messageState = remember { mutableStateOf("") }
        MessageInput(messageState = messageState, onMessageSend = {
            chatViewModel.sendMessage(it)
            messageState.value = ""
        })
    }
}

@Composable
fun messageList(
    modifier: Modifier = Modifier,
    messageList: List<MessageModel>,
    userLogo: Painter,
    talkGptLogo: Painter,
    isMessageEmpty: Boolean
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (isMessageEmpty) {
            // Show the logo in the center
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = talkGptLogo,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                )
            }
        } else {
            // Show the chat interface
            LazyColumn(
                modifier = Modifier.align(Alignment.BottomCenter),
                reverseLayout = true
            ) {
                items(messageList.reversed()) { message ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        val logo = when (message.sender) {
                            "user" -> userLogo
                            "bot" -> talkGptLogo
                            else -> userLogo // Default logo
                        }
                        BoxWithConstraints(
                            modifier = Modifier
                                .size(32.dp)
                                .layout { measurable, constraints ->
                                    val placeable = measurable.measure(constraints)
                                    layout(placeable.width, placeable.height) {
                                        placeable.place(0, 0)
                                    }
                                }
                                .clip(CircleShape)
                        ) {
                            Image(
                                painter = logo,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(minOf(maxWidth, maxHeight))
                                    .align(Alignment.Center)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (message.sender == "user") "You" else "TalkGPT",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            SelectionContainer {
                                Text(
                                    text = message.message,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}





@Composable
fun AppHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "TalkGPT",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MessageInput(messageState: MutableState<String>, onMessageSend: (String) -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageState.value,
            onValueChange = { messageState.value = it },
            modifier = Modifier.weight(1f),
            label = { Text("Enter your message") }
        )
        IconButton(onClick = { onMessageSend(messageState.value) }) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send"
            )
        }
    }
}
