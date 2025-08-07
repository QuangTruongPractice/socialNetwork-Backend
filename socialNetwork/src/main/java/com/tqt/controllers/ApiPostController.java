/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.dto.CommentDTO;
import com.tqt.dto.PostDTO;
import com.tqt.dto.PostResponseDTO;
import com.tqt.pojo.Account;
import com.tqt.pojo.Comment;
import com.tqt.pojo.Post;
import com.tqt.pojo.Reaction;
import com.tqt.pojo.User;
import com.tqt.services.AccountService;
import com.tqt.services.CommentService;
import com.tqt.services.PostService;
import com.tqt.services.ReactionService;
import com.tqt.services.UserService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
public class ApiPostController {

    @Autowired
    private PostService postService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AccountService accService;

    @Autowired
    private ReactionService reactService;

    @Autowired
    private CommentService commentService;  

    @GetMapping("/secure/posts")
    public ResponseEntity<List<PostResponseDTO>> list(@RequestParam Map<String, String> params,
            Principal principal) {
        List<Post> posts = this.postService.getPosts(params);
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        User user = this.userService.getUserById(userId);

        List<PostResponseDTO> result = posts.stream().map(post -> {
            int reactCount = this.reactService.getReactionByPostId(post.getId()).size();
            PostDTO postDTO = new PostDTO(post, reactCount);
            List<Reaction> reactions = this.reactService.getReactionByPostId(post.getId());
            postDTO.setReactionsForPost(reactions, user);
            return new PostResponseDTO(postDTO, null);
        }).toList();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable(value = "postId") int postId) {
        Post p = this.postService.getPostById(postId);
        List<Comment> comments = this.commentService.getCommentByPostId(postId);
        PostDTO postDTO = new PostDTO(p, 0);
        List<CommentDTO> commentDTOs = comments.stream().map(CommentDTO::new).toList();

        PostResponseDTO responseDTO = new PostResponseDTO(postDTO, commentDTOs);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(path = "/secure/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponseDTO> createPost(@RequestParam Map<String, String> info,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        String role = acc.getRole().name();
        Post p = this.postService.addPost(info, image, userId, role);
        PostDTO dto = new PostDTO(p, 0);
        PostResponseDTO response = new PostResponseDTO(dto, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(path = "/secure/posts/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable("postId") int postId,
            @RequestParam Map<String, String> info,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        String role = acc.getRole().name();
        
        Post existingPost = this.postService.getPostById(postId);
        if (existingPost == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        Post updatedPost = this.postService.updatePost(info, image, userId, postId, role);
        int reactCount = this.reactService.getReactionByPostId(updatedPost.getId()).size();
        PostDTO dto = new PostDTO(updatedPost, reactCount);
        PostResponseDTO response = new PostResponseDTO(dto, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable(value = "postId") int id) {
        this.postService.deletePost(id);
    }

    @PostMapping(path = "/secure/posts/survey", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponseDTO> createSurvey(@RequestParam Map<String, String> info,
            @RequestParam("options") List<String> options,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        String role = acc.getRole().name();
        
        Post p = this.postService.addSurvey(info, image, userId, role, options);
        PostDTO dto = new PostDTO(p, 0);
        PostResponseDTO response = new PostResponseDTO(dto, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/secure/posts/survey/vote")
    public ResponseEntity<?> vote(@RequestBody Map<String, Integer> body, Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        Integer optionId = body.get("optionId");
        postService.vote(userId, optionId);
        return ResponseEntity.ok().body("Voted successfully!");
    }
    
    @PostMapping(path = "/secure/posts/invitation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponseDTO> createInvitation(@RequestParam Map<String, String> info,
            @RequestParam("recipients") List<String> recipients,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Principal principal) {
        Account acc = this.accService.getAccountByEmail(principal.getName());
        Integer userId = acc.getUser().getId();
        String role = acc.getRole().name();
        
        Post p = this.postService.addInvitation(info, image, userId, role, recipients);
        PostDTO dto = new PostDTO(p, 0);
        PostResponseDTO response = new PostResponseDTO(dto, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
