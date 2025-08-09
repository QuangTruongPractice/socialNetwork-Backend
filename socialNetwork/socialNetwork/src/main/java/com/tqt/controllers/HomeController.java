/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;


import com.tqt.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Quang Truong
 */
@Controller
public class HomeController {
    
    @Autowired
    private AccountService accService;

    @RequestMapping("/admin/")
    @Transactional
    public String home(Model model){
        model.addAttribute("pendingAccounts", this.accService.getPendingAccounts());
        return "admin/home";
    }
    
    @RequestMapping("/login")
    @Transactional
    public String login(Model model){
        return "login";
    }
    
}
