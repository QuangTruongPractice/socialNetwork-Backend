/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.dto.CommentDTO;
import com.tqt.pojo.Account;
import com.tqt.pojo.Comment;
import com.tqt.services.AccountService;
import com.tqt.services.CommentService;
import java.security.Principal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Quang Truong
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiCommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AccountService accService;

    @PostMapping("/secure/posts/{postId}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable("postId") Integer postId,
            @RequestBody Map<String, String> params, Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        Comment add = commentService.addComment(params, userId, postId);
        CommentDTO addDto = new CommentDTO(add);
        return new ResponseEntity<>(addDto, HttpStatus.CREATED);
    }

    @PutMapping("/secure/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable("postId") Integer postId,
            @PathVariable("commentId") Integer commentId,
            @RequestBody Map<String, String> params,
            Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        CommentDTO updated = commentService.updateComment(params, userId, postId, commentId);
        return new ResponseEntity<CommentDTO>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/secure/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("commentId") Integer commentId, Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        this.commentService.deleteComment(commentId, userId);
    }
}
