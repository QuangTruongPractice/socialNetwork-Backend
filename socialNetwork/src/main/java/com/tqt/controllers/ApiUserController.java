/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.dto.UserDTO;
import com.tqt.pojo.Account;
import com.tqt.pojo.User;
import com.tqt.services.AccountService;
import com.tqt.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Quang Truong
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController {

    @Autowired
    private UserService userService; 
    @Autowired
    private AccountService accService; 

    @GetMapping("/secure/user")
    public ResponseEntity<?> getUser(Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        User u = this.userService.getUserById(userId);

        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @GetMapping("/secure/user/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        Object profile = this.userService.getProfileByUserId(userId);

        if (profile == null) {
            return new ResponseEntity<>(profile, HttpStatus.OK);
        }

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }
    
    @GetMapping("/users")
    public ResponseEntity<?> getAccounts(@RequestParam Map<String, String> params) {
        List<User> users = this.userService.getUsers(params);
        List<UserDTO> dto = users.stream()
                .map(user -> new UserDTO(user))
                .toList();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @PutMapping(path = "/secure/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@RequestParam Map<String, String> params,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        User u = this.userService.updateUser(params, avatar, userId);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }
}
