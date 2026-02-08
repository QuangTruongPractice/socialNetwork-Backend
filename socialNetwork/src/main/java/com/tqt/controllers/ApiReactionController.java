/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.dto.ReactDTO;
import com.tqt.pojo.Account;
import com.tqt.pojo.Notification;
import com.tqt.pojo.Post;
import com.tqt.pojo.Reaction;
import com.tqt.pojo.User;
import com.tqt.services.AccountService;
import com.tqt.services.NotificationService;
import com.tqt.services.PostService;
import com.tqt.services.ReactionService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Quang Truong
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiReactionController {

    @Autowired
    private ReactionService reactService;

    @Autowired
    private AccountService accService;

    @Autowired
    private NotificationService notiService;

    @Autowired
    private PostService postService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/posts/{postId}/reactions")
    public ResponseEntity<List<ReactDTO>> getReactionsByPost(@PathVariable(value = "postId") int postId) {
        List<Reaction> reactions = this.reactService.getReactionByPostId(postId);
        List<ReactDTO> reactDTOs = reactions.stream().map(ReactDTO::new).toList();
        return new ResponseEntity<>(reactDTOs, HttpStatus.OK);
    }

    @PutMapping("/secure/posts/{postId}/reactions")
    public ResponseEntity<?> updateReaction(
            @PathVariable("postId") Integer postId,
            @RequestBody Map<String, String> params,
            Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        User fromUser = acc.getUser();
        Reaction react = this.reactService.updateReaction(params, fromUser.getId(), postId);

        // Notify real-time if someone else reacts to my post
        Post post = postService.getPostById(postId);
        User toUser = post.getUser();
        if (toUser != null && !toUser.getId().equals(fromUser.getId())) {
            Notification n = new Notification();
            n.setFromUser(fromUser);
            n.setUser(toUser);
            n.setMessage(fromUser.getFirstName() + " " + fromUser.getLastName()
                    + " vừa bày tỏ cảm xúc về bài viết của bạn.");
            notiService.addOrUpdateNotification(n);

            // Send via WebSocket
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(toUser.getId()), "/queue/notifications", n);
        }

        return new ResponseEntity<>(new ReactDTO(react), HttpStatus.OK);
    }
}
