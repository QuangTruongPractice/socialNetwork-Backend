/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.services;

import com.tqt.pojo.Follow;
import com.tqt.pojo.User;
import java.util.List;

/**
 *
 * @author Quang Truong
 */
public interface FollowService {
    List<User> getFollowingByFollowerId(int id);
    List<User> getFollowerByFollowingId(int id);
    Follow getFollowByFollowerIdAndFollowingId(int followerId, int followingId);
    Follow addFollow(Follow f);
    void follow(int followerId, int followingId);
    void unfollow(int followerId, int followingId);
    boolean checkFollowing(int followerId, int followingId);
}
