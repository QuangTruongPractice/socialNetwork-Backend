/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.repositories;

import com.tqt.pojo.Post;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Quang Truong
 */
public interface PostRepository {
    List<Post> getPosts(Map<String, String> params);
    Integer getTotalPages(Map<String, String> params);
    Post getPostById(int id);
    List<Post> getPostByUserId(int id);
    void addOrUpdatePost(Post p);
    void deletePost(int id);
}
