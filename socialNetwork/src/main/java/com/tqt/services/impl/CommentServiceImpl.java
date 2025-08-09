/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.tqt.dto.CommentDTO;
import com.tqt.pojo.Comment;
import com.tqt.pojo.Post;
import com.tqt.pojo.User;
import com.tqt.repositories.CommentRepository;
import com.tqt.repositories.PostRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.CommentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Quang Truong
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public List<Comment> getCommentByPostId(int postId) {
        return this.commentRepo.getCommentByPostId(postId);
    }

    @Override
    public Comment getCommentById(int id) {
        return this.commentRepo.getCommentById(id);
    }

    @Override
    public Comment addComment(Map<String, String> params, Integer userId, Integer postId) {
        String content = params.get("content");
        Post p = this.postRepo.getPostById(postId);
        User u = this.userRepo.getUserById(userId);

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(p);
        comment.setUser(u);

        return this.commentRepo.addComment(comment);
    }

    @Override
    @Transactional
    public CommentDTO updateComment(Map<String, String> params, Integer userId, Integer postId, Integer commentId) {
        Comment comment = this.commentRepo.getCommentById(commentId);

        if (params.containsKey("content")) {
            comment.setContent(params.get("content"));
        }
        boolean isOwnerOfComment = comment.getUser().getId().equals(userId);

        if (!isOwnerOfComment) {
            throw new SecurityException("Không có quyền chỉnh sửa bình luận này!");
        }
        comment.setUpdatedDate(LocalDateTime.now());
        Comment updated = this.commentRepo.updateComment(comment);
        return new CommentDTO(updated);
    }

    @Override
    @Transactional
    public void deleteComment(int id, Integer userId) {
        Comment comment = this.commentRepo.getCommentById(id);

        boolean isOwnerOfComment = comment.getUser().getId().equals(userId);
        boolean isOwnerOfPost = comment.getPost().getUser().getId().equals(userId);
        
        if (!isOwnerOfComment && !isOwnerOfPost) {
            throw new SecurityException("Không có quyền xóa bình luận này!");
        }

        comment.setIsDeleted(true);
        comment.setUpdatedDate(LocalDateTime.now());
        this.commentRepo.updateComment(comment);
    }

}
