package com.tqt.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.tqt.pojo.ChatMessage;
import com.tqt.pojo.ChatRoom;
import com.tqt.pojo.ConversationDTO;
import com.tqt.pojo.User;
import com.tqt.repositories.ChatRepository;
import com.tqt.repositories.ChatRoomRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.impl.ChatServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ChatServiceTest extends BaseServiceTest {

    @Mock
    private ChatRepository chatRepo;

    @Mock
    private ChatRoomRepository roomRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private ChatServiceImpl chatService;

    // saveMessage tests - Test conditional branch (line 45)
    @Test
    public void testSaveMessage_WithSenderAndRecipient_ShouldCreateChatRoom() {
        User sender = new User();
        sender.setId(1);
        User recipient = new User();
        recipient.setId(2);

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent("Hello");

        String expectedRoomId = "1_2";
        when(roomRepo.getChatRoomById(expectedRoomId)).thenReturn(null);
        doNothing().when(roomRepo).addChatRoom(any(ChatRoom.class));
        when(chatRepo.addMessage(any(ChatMessage.class))).thenReturn(message);

        ChatMessage result = chatService.saveMessage(message);

        assertNotNull(result);
        assertNotNull(result.getTimestamp());
        verify(roomRepo).getChatRoomById(expectedRoomId);
        verify(roomRepo).addChatRoom(any(ChatRoom.class));
        verify(chatRepo).addMessage(any(ChatMessage.class));
    }

    @Test
    public void testSaveMessage_WithoutSenderOrRecipient_ShouldNotCreateChatRoom() {
        ChatMessage message = new ChatMessage();
        message.setContent("Hello");
        message.setSender(null);
        message.setRecipient(null);

        when(chatRepo.addMessage(any(ChatMessage.class))).thenReturn(message);

        ChatMessage result = chatService.saveMessage(message);

        assertNotNull(result);
        assertNotNull(result.getTimestamp());
        verify(roomRepo, never()).getChatRoomById(anyString());
        verify(roomRepo, never()).addChatRoom(any(ChatRoom.class));
        verify(chatRepo).addMessage(any(ChatMessage.class));
    }

    // getChatHistory - Simple pass-through test
    @Test
    public void testGetChatHistory_Success() {
        List<ChatMessage> messages = Arrays.asList(new ChatMessage(), new ChatMessage());
        when(chatRepo.getChatHistory(1, 2)).thenReturn(messages);

        List<ChatMessage> result = chatService.getChatHistory(1, 2);

        assertEquals(2, result.size());
        verify(chatRepo).getChatHistory(1, 2);
    }

    // getOrCreatedChatRoomId tests - Test user ID ordering (line 59) and room
    // existence (line 61)
    @Test
    public void testGetOrCreatedChatRoomId_User1LessThanUser2_RoomExists() {
        String expectedRoomId = "1_5";
        ChatRoom existingRoom = new ChatRoom();
        existingRoom.setChatRoomId(expectedRoomId);

        when(roomRepo.getChatRoomById(expectedRoomId)).thenReturn(existingRoom);

        String result = chatService.getOrCreatedChatRoomId(1, 5);

        assertEquals(expectedRoomId, result);
        verify(roomRepo).getChatRoomById(expectedRoomId);
        verify(roomRepo, never()).addChatRoom(any());
    }

    @Test
    public void testGetOrCreatedChatRoomId_User1GreaterThanUser2_RoomExists() {
        String expectedRoomId = "2_10";
        ChatRoom existingRoom = new ChatRoom();
        existingRoom.setChatRoomId(expectedRoomId);

        when(roomRepo.getChatRoomById(expectedRoomId)).thenReturn(existingRoom);

        String result = chatService.getOrCreatedChatRoomId(10, 2);

        assertEquals(expectedRoomId, result);
        verify(roomRepo).getChatRoomById(expectedRoomId);
        verify(roomRepo, never()).addChatRoom(any());
    }

    @Test
    public void testGetOrCreatedChatRoomId_RoomDoesNotExist_ShouldCreateRoom() {
        String expectedRoomId = "3_7";
        when(roomRepo.getChatRoomById(expectedRoomId)).thenReturn(null);
        doNothing().when(roomRepo).addChatRoom(any(ChatRoom.class));

        String result = chatService.getOrCreatedChatRoomId(3, 7);

        assertEquals(expectedRoomId, result);
        verify(roomRepo).getChatRoomById(expectedRoomId);
        verify(roomRepo).addChatRoom(any(ChatRoom.class));
    }

    @Test
    public void testGetOrCreatedChatRoomId_RoomDoesNotExist_ReverseOrder_ShouldCreateRoom() {
        String expectedRoomId = "5_8";
        when(roomRepo.getChatRoomById(expectedRoomId)).thenReturn(null);
        doNothing().when(roomRepo).addChatRoom(any(ChatRoom.class));

        String result = chatService.getOrCreatedChatRoomId(8, 5);

        assertEquals(expectedRoomId, result);
        verify(roomRepo).getChatRoomById(expectedRoomId);
        verify(roomRepo).addChatRoom(argThat(room -> room.getChatRoomId().equals(expectedRoomId) &&
                room.getUser1Id().equals(8) &&
                room.getUser2Id().equals(5)));
    }

    // uploadImage tests - Test null/empty files and IOException (lines 73-81)
    @Test
    public void testUploadImage_NullFile_ShouldReturnNull() {
        String result = chatService.uploadImage(null);

        assertNull(result);
        verify(cloudinary, never()).uploader();
    }

    @Test
    public void testUploadImage_EmptyFile_ShouldReturnNull() {
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);

        String result = chatService.uploadImage(emptyFile);

        assertNull(result);
        verify(cloudinary, never()).uploader();
    }

    @Test
    public void testUploadImage_Success_ShouldReturnUrl() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn(new byte[] { 1, 2, 3 });

        when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://cloudinary.com/image.jpg");
        when(uploader.upload(any(byte[].class), any())).thenReturn(uploadResult);

        String result = chatService.uploadImage(file);

        assertEquals("https://cloudinary.com/image.jpg", result);
        verify(cloudinary).uploader();
        verify(uploader).upload(any(byte[].class), any());
    }

    @Test
    public void testUploadImage_IOException_ShouldReturnNull() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenThrow(new IOException("Upload failed"));

        String result = chatService.uploadImage(file);

        assertNull(result);
    }

    // getConversations tests - Test null conditions in sorting (lines 94, 104-109)
    @Test
    public void testGetConversations_EmptyPartnersList_ShouldReturnEmptyList() {
        when(chatRepo.getRecentChatPartners(1)).thenReturn(Collections.emptyList());

        List<ConversationDTO> result = chatService.getConversations(1);

        assertTrue(result.isEmpty());
        verify(chatRepo).getRecentChatPartners(1);
        verify(userRepo, never()).getUserById(anyInt());
    }

    @Test
    public void testGetConversations_WithValidPartnersAndMessages_Success() {
        List<Integer> partnerIds = Arrays.asList(2, 3);

        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("John");

        User user3 = new User();
        user3.setId(3);
        user3.setFirstName("Jane");

        ChatMessage msg1 = new ChatMessage();
        msg1.setContent("Hello");
        msg1.setTimestamp(LocalDateTime.of(2026, 2, 8, 10, 0));

        ChatMessage msg2 = new ChatMessage();
        msg2.setContent("Hi");
        msg2.setTimestamp(LocalDateTime.of(2026, 2, 8, 12, 0));

        when(chatRepo.getRecentChatPartners(1)).thenReturn(partnerIds);
        when(userRepo.getUserById(2)).thenReturn(user2);
        when(userRepo.getUserById(3)).thenReturn(user3);
        when(chatRepo.getLastMessage(1, 2)).thenReturn(msg1);
        when(chatRepo.getLastMessage(1, 3)).thenReturn(msg2);

        List<ConversationDTO> result = chatService.getConversations(1);

        assertEquals(2, result.size());
        // Should be sorted by timestamp descending (msg2 before msg1)
        assertEquals("Hi", result.get(0).getLastMessage().getContent());
        assertEquals("Hello", result.get(1).getLastMessage().getContent());
    }

    @Test
    public void testGetConversations_WithNullUser_ShouldSkipConversation() {
        List<Integer> partnerIds = Arrays.asList(2, 3);

        User user3 = new User();
        user3.setId(3);

        ChatMessage msg2 = new ChatMessage();
        msg2.setContent("Hi");
        msg2.setTimestamp(LocalDateTime.now());

        when(chatRepo.getRecentChatPartners(1)).thenReturn(partnerIds);
        when(userRepo.getUserById(2)).thenReturn(null); // Null user
        when(userRepo.getUserById(3)).thenReturn(user3);
        when(chatRepo.getLastMessage(1, 2)).thenReturn(new ChatMessage());
        when(chatRepo.getLastMessage(1, 3)).thenReturn(msg2);

        List<ConversationDTO> result = chatService.getConversations(1);

        assertEquals(1, result.size()); // Only user3's conversation
        assertEquals("Hi", result.get(0).getLastMessage().getContent());
    }

    @Test
    public void testGetConversations_WithNullTimestamps_ShouldHandleSorting() {
        List<Integer> partnerIds = Arrays.asList(2, 3, 4);

        User user2 = new User();
        user2.setId(2);
        User user3 = new User();
        user3.setId(3);
        User user4 = new User();
        user4.setId(4);

        ChatMessage msg1 = new ChatMessage();
        msg1.setContent("A");
        msg1.setTimestamp(null); // Null timestamp

        ChatMessage msg2 = new ChatMessage();
        msg2.setContent("B");
        msg2.setTimestamp(LocalDateTime.of(2026, 2, 8, 10, 0));

        ChatMessage msg3 = new ChatMessage();
        msg3.setContent("C");
        msg3.setTimestamp(null); // Null timestamp

        when(chatRepo.getRecentChatPartners(1)).thenReturn(partnerIds);
        when(userRepo.getUserById(2)).thenReturn(user2);
        when(userRepo.getUserById(3)).thenReturn(user3);
        when(userRepo.getUserById(4)).thenReturn(user4);
        when(chatRepo.getLastMessage(1, 2)).thenReturn(msg1);
        when(chatRepo.getLastMessage(1, 3)).thenReturn(msg2);
        when(chatRepo.getLastMessage(1, 4)).thenReturn(msg3);

        List<ConversationDTO> result = chatService.getConversations(1);

        assertEquals(3, result.size());
        // msg2 (non-null) should come first
        assertEquals("B", result.get(0).getLastMessage().getContent());
        // msg1 and msg3 (both null) can be in any order after msg2
    }
}
