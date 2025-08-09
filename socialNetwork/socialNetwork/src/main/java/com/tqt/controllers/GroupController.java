/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.pojo.Group;
import com.tqt.pojo.User;
import com.tqt.services.GroupService;
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
@RequestMapping("/admin/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listGroups(@RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        Map<String, String> params = new HashMap<>();
        if (name != null && !name.isEmpty()) {
            params.put("name", name);
        }
        params.put("page", String.valueOf(page));
        model.addAttribute("groups", this.groupService.getGroups(params));
        model.addAttribute("totalPages", this.groupService.getTotalPages(params));
        model.addAttribute("currentPage", page);
        return "admin/group";
    }

    @GetMapping("/add")
    public String groupForm(Model model) {
        model.addAttribute("group", new Group());
        model.addAttribute("users", this.userService.getUsers(null));
        return "admin/group_form";
    }

    @PostMapping("/add")
    public String addGroup(@ModelAttribute(value = "group") Group g,
            @RequestParam(name = "memberIds", required = false) List<Integer> memberIds) {
        List<User> users = this.userService.findAllUserById(memberIds);
        g.setMembers(users);
        this.groupService.addOrUpdateGroup(g);
        return "redirect:/admin/groups";
    }

    @GetMapping("/edit/{groupId}")
    public String updateGroup(Model model, @PathVariable(value = "groupId") int id) {
        model.addAttribute("users", this.userService.getUsers(null));
        model.addAttribute("group", this.groupService.getGroupById(id));
        return "admin/group_form";
    }
}
