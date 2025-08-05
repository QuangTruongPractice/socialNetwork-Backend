/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
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
    private LocalSessionFactoryBean factory;
    
    @RequestMapping("/")
    @Transactional
    public String home(Model model){
        return "admin/home";
    }
    
    @RequestMapping("/login")
    @Transactional
    public String login(Model model){
        return "login";
    }
    
}
