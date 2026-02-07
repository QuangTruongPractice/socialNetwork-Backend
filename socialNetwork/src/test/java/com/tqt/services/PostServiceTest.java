package com.tqt.services;

import com.tqt.pojo.Post;
import com.tqt.pojo.User;
import com.tqt.repositories.PostRepository;
import com.tqt.services.impl.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PostServiceTest extends BaseServiceTest {

    @Mock
    private PostRepository postRepo;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    public void testUpdatePost_DifferentUserAndNotAdmin_ShouldThrowException() {
        Integer userId = 1;
        Integer otherUserId = 2;
        Integer postId = 100;

        User owner = new User();
        owner.setId(otherUserId);

        Post post = new Post();
        post.setUser(owner);

        when(postRepo.getPostById(postId)).thenReturn(post);

        Map<String, String> params = new HashMap<>();
        String role = "USER"; // Non-admin role

        // User is not post owner AND not admin -> should throw SecurityException
        assertThrows(SecurityException.class, () -> postService.updatePost(params, null, userId, postId, role));
    }
}
