package com.tqt.controllers;

import com.tqt.pojo.ChatMessage;
import com.tqt.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat/{roomId}")
    public void processMessage(
            @org.springframework.messaging.handler.annotation.DestinationVariable("roomId") String roomId,
            @Payload ChatMessage chatMessage) { // Recompile trigger
        // Persistence
        ChatMessage savedMsg = chatService.saveMessage(chatMessage);

        // Broadcast to the whole room topic
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, savedMsg);
    }
}
