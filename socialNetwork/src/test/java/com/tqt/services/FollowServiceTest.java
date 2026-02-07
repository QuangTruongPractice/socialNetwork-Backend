package com.tqt.services;

import com.tqt.pojo.Follow;
import com.tqt.pojo.Notification;
import com.tqt.pojo.User;
import com.tqt.repositories.FollowRepository;
import com.tqt.repositories.NotificationRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.impl.FollowServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FollowServiceTest extends BaseServiceTest {

    @Mock
    private FollowRepository followRepo;

    @Mock
    private NotificationRepository notiRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private FollowServiceImpl followService;

    // Simple pass-through methods - 1 test each
    @Test
    public void testGetFollowingByFollowerId_Success() {
        List<User> following = Arrays.asList(new User(), new User());
        when(followRepo.getFollowingByFollowerId(1)).thenReturn(following);

        List<User> result = followService.getFollowingByFollowerId(1);

        assertEquals(2, result.size());
        verify(followRepo).getFollowingByFollowerId(1);
    }

    @Test
    public void testGetFollowerByFollowingId_Success() {
        List<User> followers = Arrays.asList(new User());
        when(followRepo.getFollowerByFollowingId(1)).thenReturn(followers);

        List<User> result = followService.getFollowerByFollowingId(1);

        assertEquals(1, result.size());
        verify(followRepo).getFollowerByFollowingId(1);
    }

    @Test
    public void testGetFollowByFollowerIdAndFollowingId_Success() {
        Follow follow = new Follow();
        when(followRepo.getFollowByFollowerIdAndFollowingId(1, 2)).thenReturn(follow);

        Follow result = followService.getFollowByFollowerIdAndFollowingId(1, 2);

        assertEquals(follow, result);
        verify(followRepo).getFollowByFollowerIdAndFollowingId(1, 2);
    }

    @Test
    public void testAddFollow_Success() {
        Follow follow = new Follow();
        when(followRepo.addFollow(follow)).thenReturn(follow);

        Follow result = followService.addFollow(follow);

        assertEquals(follow, result);
        verify(followRepo).addFollow(follow);
    }

    @Test
    public void testUnfollow_Success() {
        followService.unfollow(1, 2);

        verify(followRepo).deleteFollow(1, 2);
    }

    @Test
    public void testCheckFollowing_Success() {
        when(followRepo.checkFollowing(1, 2)).thenReturn(true);

        boolean result = followService.checkFollowing(1, 2);

        assertTrue(result);
        verify(followRepo).checkFollowing(1, 2);
    }

    // Complex method: follow - Test conditional branch (self-follow prevention)
    @Test
    public void testFollow_SelfFollow_ShouldDoNothing() {
        followService.follow(1, 1);

        verify(userRepo, never()).getUserById(anyInt());
        verify(followRepo, never()).addFollow(any());
        verify(notiRepo, never()).addOrUpdateNotification(any());
    }

    @Test
    public void testFollow_DifferentUsers_ShouldCreateFollowAndNotification() {
        User follower = new User();
        follower.setId(1);
        follower.setFirstName("John");
        follower.setLastName("Doe");

        User following = new User();
        following.setId(2);
        following.setFirstName("Jane");
        following.setLastName("Smith");

        when(userRepo.getUserById(1)).thenReturn(follower);
        when(userRepo.getUserById(2)).thenReturn(following);
        when(followRepo.addFollow(any(Follow.class))).thenReturn(new Follow());
        doNothing().when(notiRepo).addOrUpdateNotification(any(Notification.class));

        followService.follow(1, 2);

        verify(userRepo).getUserById(1);
        verify(userRepo).getUserById(2);
        verify(followRepo).addFollow(any(Follow.class));
        verify(notiRepo).addOrUpdateNotification(any(Notification.class));
    }
}
