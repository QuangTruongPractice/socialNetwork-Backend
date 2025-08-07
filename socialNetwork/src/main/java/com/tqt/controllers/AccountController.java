/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.pojo.Account;
import com.tqt.pojo.User;
import com.tqt.services.AccountService;
import com.tqt.services.UserService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String listAccounts(@RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        Map<String, String> params = new HashMap<>();
        if (email != null && !email.isEmpty()) {
            params.put("email", email);
        }
        params.put("page", String.valueOf(page));
        model.addAttribute("accounts", this.accService.getAccounts(params));
        model.addAttribute("totalPages", this.accService.getTotalPages(params));
        model.addAttribute("currentPage", page);
        return "admin/account";
    }

    @GetMapping("/add")
    public String accoountForm(Model model) {
        Account account = new Account();
        account.setIsActive(true);
        account.setIsVerified(true);

        model.addAttribute("account", account);
        model.addAttribute("users", this.userService.findUsersWithoutAccount());
        return "admin/account_form";
    }

    @PostMapping("/add")
    public String addAccount(@ModelAttribute(value = "account") @Valid Account a, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", this.userService.findUsersWithoutAccount());
            return "admin/account_form";
        }
        try {
            this.accService.addOrUpdateAccount(a);
        } catch (RuntimeException ex) {
            result.rejectValue("email", "error.account", ex.getMessage());
            model.addAttribute("users", this.userService.findUsersWithoutAccount());
            return "admin/account_form";
        }
        return "redirect:/admin/accounts";
    }

    @GetMapping("/edit/{accountId}")
    public String updateUser(Model model, @PathVariable(value = "accountId") int id) {
        Account acc = this.accService.getAccountById(id);
        List<User> users = userService.findUsersWithoutAccount();
        if (acc.getUser() != null && !users.contains(acc.getUser())) {
            users.add(acc.getUser());
        }
        model.addAttribute("account", acc);
        model.addAttribute("users", users);
        return "admin/account_form";
    }

    @PostMapping("/approve/{id}")
    public String approveAccount(@PathVariable("id") int id) {
        this.accService.approveAccount(id);
        return "redirect:/admin/accounts";
    }
}
