/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.services;

import com.tqt.pojo.Reaction;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Quang Truong
 */
public interface ReactionService {
    List<Reaction> getReactionByPostId(int postId);
    Reaction getReactionByUserAndPost(int userId, int postId);
    Reaction updateReaction(Map<String, String> params, Integer userId, Integer postId);
}
