/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.pojo.User;
import com.tqt.services.UserService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Quang Truong
 */
@Controller
@RequestMapping("/admin/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(@RequestParam(name = "userCode", required = false) String userCode, Model model) {
        Map<String, String> params = new HashMap<>();
        if (userCode != null && !userCode.isEmpty()) {
            params.put("userCode", userCode);
        }

        model.addAttribute("users", this.userService.getUsers(params)); // ✅ đổ dữ liệu
        return "admin/user";
    }
    @GetMapping("/add")
    public String userForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user_form"; 
    }
    
    @PostMapping("/add")
    public String addUser(@ModelAttribute(value = "user") User u) {
        this.userService.addOrUpdateUser(u);
        return "redirect:/admin/users"; 
    }
    
    @GetMapping("/edit/{userId}")
    public String updateUser(Model model, @PathVariable(value = "userId") int id ) {
        model.addAttribute("user", this.userService.getUserById(id));
        return "admin/user_form";
    }
}
