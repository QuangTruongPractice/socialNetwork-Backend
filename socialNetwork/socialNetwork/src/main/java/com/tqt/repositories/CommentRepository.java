/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.repositories;

import com.tqt.pojo.Comment;
import java.util.List;

/**
 *
 * @author Quang Truong
 */
public interface CommentRepository {
    List<Comment> getCommentByPostId(int postId);
    Comment getCommentById(int id);
    Comment addComment(Comment c);
    Comment updateComment(Comment c);
}
