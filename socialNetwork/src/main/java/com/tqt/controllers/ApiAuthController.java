/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.dto.RegisterDTO;
import com.tqt.pojo.Account;
import com.tqt.services.AccountService;
import com.tqt.services.RegisterService;
import com.tqt.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public class ApiAuthController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private AccountService accService;

    @PostMapping(path = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestParam Map<String, String> info,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        try {
            RegisterDTO response = this.registerService.register(info, avatar);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account a) {

        if (this.accService.authenticate(a.getEmail(), a.getPassword())) {
            try {
                Account accFromDb = this.accService.getAccountByEmail(a.getEmail());
                if (accFromDb.getIsActive() == true && accFromDb.getIsVerified() == true) {
                    String token = JwtUtils.generateToken(
                            accFromDb.getEmail(),
                            accFromDb.getRole().name(),
                            accFromDb.getUser().getId(),
                            accFromDb.getMustChangePassword()
                    );
                    return ResponseEntity.ok().body(Collections.singletonMap("token", token));
                } else {
                    return ResponseEntity.badRequest().body("Tài khoản chưa được duyệt hoặc đã bị khóa");
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Lỗi khi tạo JWT: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }

    @PostMapping("/secure/change-password")
    public ResponseEntity<?> getProfile(HttpServletRequest request,
            @RequestBody Map<String, String> body) {
        String email = (String) request.getAttribute("email");
        Account acc = this.accService.getAccountByEmail(email);
        return new ResponseEntity<>(this.registerService.changePassword(body, acc), HttpStatus.OK);
    }

    @GetMapping("/secure/principal")
    public ResponseEntity<?> principal(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Không có principal");
        }

        Map<String, Object> res = new HashMap<>();
        res.put("name", principal.getName());
        res.put("details", principal.toString());

        return ResponseEntity.ok(res);
    }

}
