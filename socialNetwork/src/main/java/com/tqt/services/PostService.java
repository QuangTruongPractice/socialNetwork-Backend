/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.services;

import com.tqt.pojo.Post;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Quang Truong
 */
public interface PostService {
    List<Post> getPosts(Map<String, String> params);

    Integer getTotalPages(Map<String, String> params);

    Post getPostById(int id);

    List<Post> getPostByUserId(int id);

    void addOrUpdatePost(Post p);

    void deletePost(int id);

    Post addPost(Map<String, String> params, List<MultipartFile> images, Integer userId, String role);

    Post updatePost(Map<String, String> params, List<MultipartFile> images, Integer userId, Integer postId,
            String role);

    Post addSurvey(Map<String, String> params, List<MultipartFile> images, Integer userId, String role,
            List<String> options);

    Post addInvitation(Map<String, String> params, List<MultipartFile> images, Integer userId, String role,
            List<String> recipients);

    void vote(Integer userId, Integer optionId);
}
