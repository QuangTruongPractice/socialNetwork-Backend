/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.pojo.Post;
import com.tqt.services.PostService;
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
@RequestMapping("/admin/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

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
    public String postForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("users", this.userService.getUsers(null));
        return "admin/post_form";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute(value = "post") Post p) {
        this.postService.addOrUpdatePost(p);
        return "redirect:/admin/posts";
    }

    @GetMapping("/edit/{postId}")
    public String updateUser(Model model, @PathVariable(value = "postId") int id) {
        model.addAttribute("users", this.userService.getUsers(null));
        model.addAttribute("post", this.postService.getPostById(id));
        return "admin/post_form";
    }

    @GetMapping("/delete/{postId}")
    public String deletePost(@PathVariable("postId") int id) {
        this.postService.deletePost(id);
        return "redirect:/admin/posts";
    }
}
