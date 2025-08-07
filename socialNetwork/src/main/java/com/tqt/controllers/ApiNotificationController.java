/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.pojo.Account;
import com.tqt.pojo.Notification;
import com.tqt.services.AccountService;
import com.tqt.services.NotificationService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Quang Truong
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiNotificationController {

    @Autowired
    private NotificationService notiService;
    
    @Autowired
    private AccountService accService;

    @GetMapping("/secure/notifications")
    public ResponseEntity<List<Notification>> getNotifications(Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        return ResponseEntity.ok(notiService.getNotificationByUserId(userId));
    }

    @PutMapping("/notifications/{id}/read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable(value = "id") int id) {
        this.notiService.read(id);
        return ResponseEntity.ok("Mark read success");
    }
}
