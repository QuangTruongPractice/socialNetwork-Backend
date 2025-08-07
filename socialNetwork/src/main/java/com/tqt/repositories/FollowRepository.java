/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.repositories;

import com.tqt.pojo.Follow;
import com.tqt.pojo.User;
import java.util.List;

/**
 *
 * @author Quang Truong
 */
public interface FollowRepository {
    List<User> getFollowingByFollowerId(int id);
    List<User> getFollowerByFollowingId(int id);
    Follow getFollowByFollowerIdAndFollowingId(int followerId, int followingId);
    Follow addFollow(Follow f);
    void deleteFollow(int followerId, int followingId);
    boolean checkFollowing(int followerId, int followingId);
}
