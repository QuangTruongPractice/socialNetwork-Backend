package com.tqt.repositories;

import com.tqt.pojo.ChatRoom;
import java.util.List;

public interface ChatRoomRepository {
    ChatRoom getChatRoomById(String chatRoomId);

    void addChatRoom(ChatRoom room);

    List<ChatRoom> getRoomsByUser(Integer userId);
}
