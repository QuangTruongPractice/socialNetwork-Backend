package com.tqt.repositories;

import com.tqt.pojo.ChatMessage;
import java.util.List;

public interface ChatRepository {
    ChatMessage addMessage(ChatMessage message);

    List<ChatMessage> getChatHistory(Integer user1, Integer user2);

    ChatMessage getLastMessage(Integer user1, Integer user2);

    List<Integer> getRecentChatPartners(Integer userId);
}
