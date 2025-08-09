/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.pojo.Account;
import com.tqt.pojo.User;
import com.tqt.services.AccountService;
import com.tqt.services.FollowService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Quang Truong
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiFollowController {

    @Autowired
    private FollowService followService;
    
    @Autowired
    private AccountService accService;

    @GetMapping("/secure/follows/followings")
    public ResponseEntity<List<User>> getFollowings(Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        List<User> followings = this.followService.getFollowingByFollowerId(userId);
        return new ResponseEntity<>(followings, HttpStatus.OK);
    }

    @GetMapping("/secure/follows/followers")
    public ResponseEntity<List<User>> getFollowers(Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        List<User> followers = this.followService.getFollowerByFollowingId(userId);
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    @PostMapping("/secure/follows/{followingId}")
    public ResponseEntity<?> follow(Principal principal,
            @PathVariable(value = "followingId") int followingId) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        this.followService.follow(userId, followingId);
        return ResponseEntity.ok("Followed successfully");
    }

    @DeleteMapping("/secure/follows/{followingId}")
    public ResponseEntity<?> unfollow(Principal principal,
            @PathVariable(value = "followingId") int followingId) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        this.followService.unfollow(userId, followingId);
        return ResponseEntity.ok("Unfollowed successfully");
    }

    @GetMapping("/secure/follows/check/{followId}")
    public ResponseEntity<?> checkFollow(@PathVariable(value = "followId") int followId, Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        boolean isFollowing = this.followService.checkFollowing(userId, followId);
        return ResponseEntity.ok(Map.of("following", isFollowing));
    }
}
