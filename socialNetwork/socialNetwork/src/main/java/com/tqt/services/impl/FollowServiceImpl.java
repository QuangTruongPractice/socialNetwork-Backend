/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.tqt.pojo.Follow;
import com.tqt.pojo.Notification;
import com.tqt.pojo.User;
import com.tqt.repositories.FollowRepository;
import com.tqt.repositories.NotificationRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.FollowService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Quang Truong
 */
@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepository followRepo;

    @Autowired
    private NotificationRepository notiRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public List<User> getFollowingByFollowerId(int id) {
        return this.followRepo.getFollowingByFollowerId(id);
    }

    @Override
    public List<User> getFollowerByFollowingId(int id) {
        return this.followRepo.getFollowerByFollowingId(id);
    }

    @Override
    public Follow getFollowByFollowerIdAndFollowingId(int followerId, int followingId) {
        return this.followRepo.getFollowByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Override
    public Follow addFollow(Follow f) {
        return this.followRepo.addFollow(f);
    }

    @Override
    public void unfollow(int followerId, int followingId) {
        this.followRepo.deleteFollow(followerId, followingId);
    }

    @Override
    public void follow(int followerId, int followingId) {
        if (followerId == followingId) {
            return;
        }
        User follower = this.userRepo.getUserById(followerId);
        User following = this.userRepo.getUserById(followingId);
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        this.followRepo.addFollow(follow);
        String message = follower.getFirstName() + " " + follower.getLastName() + " đã theo dõi bạn.";
        Notification n = new Notification();
        n.setFromUser(follower);
        n.setUser(following);
        n.setMessage(message);
        this.notiRepo.addOrUpdateNotification(n);
    }

    @Override
    public boolean checkFollowing(int followerId, int followingId) {
        return this.followRepo.checkFollowing(followerId, followingId);
    }

}
