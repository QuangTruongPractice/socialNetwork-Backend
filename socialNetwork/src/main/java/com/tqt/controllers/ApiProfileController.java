/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.dto.PostDTO;
import com.tqt.dto.ProfileResponseDTO;
import com.tqt.pojo.Account;
import com.tqt.pojo.Reaction;
import com.tqt.pojo.User;
import com.tqt.services.AccountService;
import com.tqt.services.AdminProfileService;
import com.tqt.services.AlumniProfileService;
import com.tqt.services.LecturerProfileService;
import com.tqt.services.PostService;
import com.tqt.services.ReactionService;
import com.tqt.services.UserService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@Transactional
public class ApiProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accService;

    @Autowired
    private PostService postService;

    @Autowired
    private AlumniProfileService alumniService;

    @Autowired
    private AdminProfileService adminService;

    @Autowired
    private LecturerProfileService lecturerService;

    @Autowired
    private ReactionService reactService;

    @GetMapping("/secure/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        String role = acc.getRole().name();

        System.out.println(role);

        Object profile;
        User user = userService.getUserById(userId);
        List<PostDTO> postDTOs = postService.getPostByUserId(userId).stream().map(post -> {
            List<Reaction> reactions = this.reactService.getReactionByPostId(post.getId());
            int reactCount = reactions.size();
            PostDTO postDTO = new PostDTO(post, reactCount);
            postDTO.setReactionsForPost(reactions, user);
            return postDTO;
        }).collect(Collectors.toList());

        switch (role.toUpperCase()) {
            case "ALUMNI":
                profile = this.alumniService.getAlumniProfileByUserId(userId);
                break;
            case "ADMIN":
                profile = this.adminService.getAdminProfileByUserId(userId);
                break;
            case "LECTURER":
                profile = this.lecturerService.getLecturerProfileByUserId(userId);
                break;
            default:
                return ResponseEntity.badRequest().body("Unsupported role: " + role);
        }

        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found for userId: " + userId);
        }

        ProfileResponseDTO responseDTO = new ProfileResponseDTO(profile, user, postDTOs, role);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/secure/profile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable("userId") Integer userId,
            Principal principal) {
        Account currentAcc = this.accService.getAccountByEmail(principal.getName());
        Integer currentUserId = currentAcc.getUser().getId();
        User currentUser = this.userService.getUserById(currentUserId);

        Account acc = this.accService.getAccountByUserId(userId);
        User user = this.userService.getUserById(userId);
        String role = acc.getRole().name();
        System.out.println(role);

        Object profile;
        List<PostDTO> postDTOs = postService.getPostByUserId(userId).stream().map(post -> {
            List<Reaction> reactions = this.reactService.getReactionByPostId(post.getId());
            int reactCount = reactions.size();
            PostDTO postDTO = new PostDTO(post, reactCount);
            postDTO.setReactionsForPost(reactions, currentUser);
            return postDTO;
        }).collect(Collectors.toList());

        switch (role.toUpperCase()) {
            case "ALUMNI":
                profile = this.alumniService.getAlumniProfileByUserId(userId);
                break;
            case "ADMIN":
                profile = this.adminService.getAdminProfileByUserId(userId);
                break;
            case "LECTURER":
                profile = this.lecturerService.getLecturerProfileByUserId(userId);
                break;
            default:
                return ResponseEntity.badRequest().body("Unsupported role: " + role);
        }

        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found for userId: " + userId);
        }

        ProfileResponseDTO responseDTO = new ProfileResponseDTO(profile, user, postDTOs, role);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(path = "/secure/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProfile(@RequestParam Map<String, String> params,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
            Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        String role = acc.getRole().name();
        Object profile;

        switch (role.toUpperCase()) {
            case "ALUMNI":
                profile = this.alumniService.addAlumniProfile(params, coverImage, userId);
                break;
            case "ADMIN":
                profile = this.adminService.addAdminProfile(params, coverImage, userId);
                break;
            case "LECTURER":
                profile = this.lecturerService.addLecturerProfile(params, coverImage, userId);
                break;
            default:
                return ResponseEntity.badRequest().body("Unsupported role: " + role);
        }

        return new ResponseEntity<>(profile, HttpStatus.CREATED);
    }

    @PutMapping(path = "/secure/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@RequestParam Map<String, String> params,
            @RequestParam("coverImage") MultipartFile coverImage,
            Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        String role = acc.getRole().name();
        Object profile;

        switch (role.toUpperCase()) {
            case "ALUMNI":
                profile = this.alumniService.updateAlumniProfile(params, coverImage, userId);
                break;
            case "ADMIN":
                profile = this.adminService.updateAdminProfile(params, coverImage, userId);
                break;
            case "LECTURER":
                profile = this.lecturerService.updateLecturerProfile(params, coverImage, userId);
                break;
            default:
                return ResponseEntity.badRequest().body("Unsupported role: " + role);
        }

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }
}
