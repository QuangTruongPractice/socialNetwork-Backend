package com.tqt.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tqt.pojo.ChatMessage;
import com.tqt.pojo.ChatRoom;
import com.tqt.pojo.ConversationDTO;
import com.tqt.pojo.User;
import com.tqt.repositories.ChatRepository;
import com.tqt.repositories.ChatRoomRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.ChatService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepo;

    @Autowired
    private ChatRoomRepository roomRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public ChatMessage saveMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());

        // Ensure ChatRoom exists for the contact list to find this conversation
        if (message.getSender() != null && message.getRecipient() != null) {
            getOrCreatedChatRoomId(message.getSender().getId(), message.getRecipient().getId());
        }

        return chatRepo.addMessage(message);
    }

    @Override
    public List<ChatMessage> getChatHistory(Integer user1, Integer user2) {
        return chatRepo.getChatHistory(user1, user2);
    }

    @Override
    public String getOrCreatedChatRoomId(Integer user1, Integer user2) {
        String roomId = user1 < user2 ? user1 + "_" + user2 : user2 + "_" + user1;
        ChatRoom room = roomRepo.getChatRoomById(roomId);
        if (room == null) {
            ChatRoom newRoom = new ChatRoom();
            newRoom.setChatRoomId(roomId);
            newRoom.setUser1Id(user1);
            newRoom.setUser2Id(user2);
            roomRepo.addChatRoom(newRoom);
        }
        return roomId;
    }

    @Override
    public String uploadImage(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                Map<String, Object> res = (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                return res.get("secure_url").toString();
            } catch (IOException ex) {
                System.err.println("Chat image upload error: " + ex.getMessage());
            }
        }
        return null;
    }

    @Override
    public List<ConversationDTO> getConversations(Integer userId) {
        List<Integer> partnerIds = chatRepo.getRecentChatPartners(userId);
        List<ConversationDTO> conversations = new ArrayList<>();

        for (Integer otherUserId : partnerIds) {
            User otherUser = userRepo.getUserById(otherUserId);
            ChatMessage lastMsg = chatRepo.getLastMessage(userId, otherUserId);

            if (lastMsg != null && otherUser != null) {
                conversations.add(new ConversationDTO(otherUser, lastMsg));
            }
        }

        // Sort by last message timestamp descending
        return conversations.stream()
                .sorted((c1, c2) -> {
                    LocalDateTime t1 = c1.getLastMessage().getTimestamp();
                    LocalDateTime t2 = c2.getLastMessage().getTimestamp();
                    if (t1 == null && t2 == null)
                        return 0;
                    if (t1 == null)
                        return 1;
                    if (t2 == null)
                        return -1;
                    return t2.compareTo(t1);
                })
                .collect(Collectors.toList());
    }
}
