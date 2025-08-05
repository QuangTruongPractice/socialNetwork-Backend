/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.pojo.Account;
import com.tqt.pojo.User;
import com.tqt.services.AccountService;
import com.tqt.services.UserService;
import java.util.HashMap;
import java.util.List;
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
@RequestMapping("/admin/accounts")
public class AccountController {

    @Autowired
    private AccountService accService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listAccounts(@RequestParam(name = "email", required = false) String email, Model model) {
        Map<String, String> params = new HashMap<>();
        if (email != null && !email.isEmpty()) {
            params.put("email", email);
        }

        model.addAttribute("accounts", this.accService.getAccounts(params)); // ✅ đổ dữ liệu
        return "admin/account";
    }

    @GetMapping("/add")
    public String accoountForm(Model model) {
        Account account = new Account();
        account.setIsActive(true);    // Mặc định tick
        account.setIsVerified(true);  // Mặc định tick

        model.addAttribute("account", account);
        model.addAttribute("users", userService.findUsersWithoutAccount());
        System.out.println("🚀 AccountScheduler được tạo");
        return "admin/account_form";
    }

    @PostMapping("/add")
    public String addAccount(@ModelAttribute(value = "account") Account a) {
        this.accService.addOrUpdateAccount(a);
        return "redirect:/admin/accounts";
    }

    @GetMapping("/edit/{accountId}")
    public String updateUser(Model model, @PathVariable(value = "accountId") int id) {
        Account acc = this.accService.getAccountById(id);
        List<User> users = userService.findUsersWithoutAccount();
        // Nếu user hiện tại không có trong danh sách => thêm vào
        if (acc.getUser() != null && !users.contains(acc.getUser())) {
            users.add(acc.getUser());
        }
        System.out.println("🚀 AccountScheduler được tạo");

        model.addAttribute("account", acc);
        model.addAttribute("users", users);
        return "admin/account_form";
    }
}
