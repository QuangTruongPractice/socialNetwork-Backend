/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.tqt.enums.ReactionType;
import com.tqt.pojo.Post;
import com.tqt.pojo.Reaction;
import com.tqt.pojo.User;
import com.tqt.repositories.PostRepository;
import com.tqt.repositories.ReactionRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.ReactionService;
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
public class ReactionServiceImpl implements ReactionService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private ReactionRepository reactRepo;

    @Override
    public List<Reaction> getReactionByPostId(int postId) {
        return this.reactRepo.getReactionByPostId(postId);
    }

    @Override
    public Reaction getReactionByUserAndPost(int userId, int postId) {
        return this.reactRepo.getReactionByUserAndPost(userId, postId);
    }

    @Override
    @Transactional
    public Reaction updateReaction(Map<String, String> params, Integer userId, Integer postId) {
        String reactionType = params.get("reactionType").toUpperCase();
        ReactionType type = ReactionType.valueOf(reactionType);

        Reaction existing = this.reactRepo.getReactionByUserAndPost(userId, postId);

        if (existing != null) {
            existing.setReactionType(type);
            return this.reactRepo.updateReaction(existing);
        } else {
            Post p = this.postRepo.getPostById(postId);
            User u = this.userRepo.getUserById(userId);

            Reaction newReact = new Reaction();
            newReact.setReactionType(type);
            newReact.setPost(p);
            newReact.setUser(u);

            return this.reactRepo.addReaction(newReact);
        }
    }

}
