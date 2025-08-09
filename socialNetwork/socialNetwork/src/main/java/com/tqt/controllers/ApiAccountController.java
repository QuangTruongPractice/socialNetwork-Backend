/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.dto.AccountResponseDTO;
import com.tqt.pojo.Account;
import com.tqt.services.AccountService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Quang Truong
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiAccountController {

    @Autowired
    private AccountService accService;

    @GetMapping("/accounts")
    public ResponseEntity<?> getAccounts(@RequestParam Map<String, String> params) {
        List<Account> accounts = this.accService.getAccounts(params);
        List<AccountResponseDTO> dto = accounts.stream()
                .map(account -> new AccountResponseDTO(account))
                .toList();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
