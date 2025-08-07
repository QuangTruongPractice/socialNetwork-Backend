/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.services;

import com.tqt.dto.CommentDTO;
import com.tqt.pojo.Comment;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Quang Truong
 */
public interface CommentService {
    List<Comment> getCommentByPostId(int postId);
    Comment getCommentById(int id);
    Comment addComment(Map<String, String> params, Integer userId, Integer postId);
    CommentDTO updateComment(Map<String, String> params, Integer userId, Integer postId, Integer commentId);
    void deleteComment(int id, Integer userId);
}
