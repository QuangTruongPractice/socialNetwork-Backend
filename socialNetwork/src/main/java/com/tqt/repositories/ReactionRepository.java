/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.repositories;

import com.tqt.pojo.Reaction;
import java.util.List;

/**
 *
 * @author Quang Truong
 */
public interface ReactionRepository {
    List<Reaction> getReactionByPostId(int postId);
    Reaction getReactionByUserAndPost(int userId, int postId);
    Reaction addReaction(Reaction r);
    Reaction updateReaction(Reaction r);
}
