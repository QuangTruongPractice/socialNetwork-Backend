package com.tqt.controllers;

import com.tqt.pojo.Account;
import com.tqt.pojo.ChatMessage;
import com.tqt.pojo.ConversationDTO;
import com.tqt.services.AccountService;
import com.tqt.services.ChatService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/secure/chat")
@CrossOrigin
public class ApiChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private AccountService accService;

    @GetMapping("/history/{user1}/{user2}")
    public ResponseEntity<List<ChatMessage>> getHistory(
            @PathVariable("user1") Integer user1,
            @PathVariable("user2") Integer user2) {
        return ResponseEntity.ok(chatService.getChatHistory(user1, user2));
    }

    @GetMapping("/room/{user1}/{user2}")
    public ResponseEntity<String> getRoomId(
            @PathVariable("user1") Integer user1,
            @PathVariable("user2") Integer user2) {
        return ResponseEntity.ok(chatService.getOrCreatedChatRoomId(user1, user2));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = chatService.uploadImage(file);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/conversations")
    public ResponseEntity<?> getConversations(Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        if (acc == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(chatService.getConversations(acc.getUser().getId()));
    }
}
