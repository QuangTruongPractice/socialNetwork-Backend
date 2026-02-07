/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.enums.PostType;
import com.tqt.pojo.Account;
import com.tqt.pojo.Post;
import com.tqt.services.AccountService;
import com.tqt.services.GroupService;
import com.tqt.services.PostService;
import com.tqt.services.UserService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Quang Truong
 */
@Controller
@RequestMapping("/admin/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private AccountService accService;

    @GetMapping
    public String listPosts(
            @RequestParam(name = "userName", required = false) String userName,
            @RequestParam(name = "createdAt", required = false) String createdAt,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            Model model) {

        Map<String, String> params = new HashMap<>();

        if (userName != null && !userName.isEmpty()) {
            params.put("userName", userName);
        }

        if (createdAt != null && !createdAt.isEmpty()) {
            params.put("createdAt", createdAt);
        }
        params.put("page", String.valueOf(page));

        model.addAttribute("posts", this.postService.getPosts(params));
        model.addAttribute("totalPages", this.postService.getTotalPages(params));
        model.addAttribute("currentPage", page);

        return "admin/post";
    }

    @GetMapping("/add")
    public String postForm(Model model, Principal principal) {
        model.addAttribute("post", new Post());
        model.addAttribute("users", this.userService.getUsers(null));
        model.addAttribute("groups", this.groupService.getGroups(null));
        model.addAttribute("accounts", this.accService.getAccounts(null));
        model.addAttribute("postTypes", PostType.values());
        model.addAttribute("currentUser", this.accService.getAccountByEmail(principal.getName()).getUser());
        return "admin/post_form";
    }

    @PostMapping("/add")
    public String addPost(
            @RequestParam("content") String content,
            @RequestParam("postType") String postType,
            @RequestParam(value = "image", required = false) List<MultipartFile> image,
            @RequestParam(value = "surveyOptions", required = false) String[] surveyOptions,
            @RequestParam(value = "recipients", required = false) String[] recipients,
            Principal principal) {

        System.out.println("=== Nhận request /addPost ===");
        System.out.println("Content: " + content);
        System.out.println("PostType: " + postType);
        System.out.println("SurveyOptions: " + (surveyOptions != null ? Arrays.toString(surveyOptions) : "null"));
        System.out.println("Recipients: " + (recipients != null ? Arrays.toString(recipients) : "null"));
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();

        Map<String, String> params = new HashMap<>();
        params.put("content", content);

        String role = "ADMIN";

        if ("SURVEY".equals(postType)) {
            List<String> optionsList = new ArrayList<>();
            if (surveyOptions != null) {
                for (String option : surveyOptions) {
                    if (option != null && !option.trim().isEmpty()) {
                        optionsList.add(option.trim());
                    }
                }
            }
            this.postService.addSurvey(params, image, userId, role, optionsList);
        } else if ("INVITATION".equals(postType)) {
            List<String> recipientsList = recipients != null ? Arrays.asList(recipients) : new ArrayList<>();
            this.postService.addInvitation(params, image, userId, role, recipientsList);
        } else {
            this.postService.addPost(params, image, userId, role);
        }

        return "redirect:/admin/posts";
    }

    @GetMapping("/edit/{postId}")
    public String updatePost(Model model, @PathVariable(value = "postId") int id) {
        model.addAttribute("users", this.userService.getUsers(null));
        model.addAttribute("post", this.postService.getPostById(id));
        model.addAttribute("groups", this.groupService.getGroups(null));
        model.addAttribute("accounts", this.accService.getAccounts(null));
        model.addAttribute("postTypes", PostType.values());
        return "admin/post_form";
    }

    @GetMapping("/delete/{postId}")
    public String deletePost(@PathVariable("postId") int id) {
        this.postService.deletePost(id);
        return "redirect:/admin/posts";
    }
}
