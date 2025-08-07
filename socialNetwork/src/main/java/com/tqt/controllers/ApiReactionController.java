package com.tqt.controllers;

import com.tqt.dto.ReactDTO;
import com.tqt.pojo.Account;
import com.tqt.pojo.Reaction;
import com.tqt.services.AccountService;
import com.tqt.services.ReactionService;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    @GetMapping("/posts/{postId}/reactions")
    public ResponseEntity<List<ReactDTO>> getReactionsByPost(@PathVariable(value = "postId") int postId) {
        List<Reaction> reactions = this.reactService.getReactionByPostId(postId);
        List<ReactDTO> reactDTOs = reactions.stream().map(ReactDTO::new).toList();
        return new ResponseEntity<>(reactDTOs, HttpStatus.OK);
    }

    @PutMapping("/secure/posts/{postId}/reactions")
    public ResponseEntity<?> updateReaction(
            @PathVariable(value = "postId") Integer postId,
            @RequestBody Map<String, String> params,
            Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        Reaction react = this.reactService.updateReaction(params, userId, postId);
        return new ResponseEntity<>(new ReactDTO(react), HttpStatus.OK);
    }
}
