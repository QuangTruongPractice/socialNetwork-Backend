package com.tqt.services;

import com.tqt.pojo.ChatMessage;
import com.tqt.pojo.ConversationDTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ChatService {
    ChatMessage saveMessage(ChatMessage message);

    List<ChatMessage> getChatHistory(Integer user1, Integer user2);

    String getOrCreatedChatRoomId(Integer user1, Integer user2);

    String uploadImage(MultipartFile file);

    List<ConversationDTO> getConversations(Integer userId);
}
