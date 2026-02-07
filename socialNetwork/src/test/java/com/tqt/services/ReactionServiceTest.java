package com.tqt.services;

import com.tqt.enums.ReactionType;
import com.tqt.pojo.Post;
import com.tqt.pojo.Reaction;
import com.tqt.pojo.User;
import com.tqt.repositories.PostRepository;
import com.tqt.repositories.ReactionRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.impl.ReactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReactionServiceTest extends BaseServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PostRepository postRepo;

    @Mock
    private ReactionRepository reactRepo;

    @InjectMocks
    private ReactionServiceImpl reactionService;

    // Simple pass-through methods - 1 test each
    @Test
    public void testGetReactionByPostId_Success() {
        List<Reaction> reactions = Arrays.asList(new Reaction(), new Reaction());
        when(reactRepo.getReactionByPostId(1)).thenReturn(reactions);

        List<Reaction> result = reactionService.getReactionByPostId(1);

        assertEquals(2, result.size());
        verify(reactRepo).getReactionByPostId(1);
    }

    @Test
    public void testGetReactionByUserAndPost_Success() {
        Reaction reaction = new Reaction();
        when(reactRepo.getReactionByUserAndPost(1, 2)).thenReturn(reaction);

        Reaction result = reactionService.getReactionByUserAndPost(1, 2);

        assertEquals(reaction, result);
        verify(reactRepo).getReactionByUserAndPost(1, 2);
    }

    // Complex method: updateReaction - Test both branches (existing vs new)
    @Test
    public void testUpdateReaction_ReactionExists_ShouldUpdateType() {
        Map<String, String> params = new HashMap<>();
        params.put("reactionType", "love");

        Reaction existing = new Reaction();
        existing.setReactionType(ReactionType.LIKE);

        when(reactRepo.getReactionByUserAndPost(1, 2)).thenReturn(existing);
        when(reactRepo.updateReaction(any(Reaction.class))).thenReturn(existing);

        Reaction result = reactionService.updateReaction(params, 1, 2);

        assertEquals(ReactionType.LOVE, existing.getReactionType());
        verify(reactRepo).updateReaction(existing);
        verify(reactRepo, never()).addReaction(any());
    }

    @Test
    public void testUpdateReaction_ReactionNotExists_ShouldCreateNew() {
        Map<String, String> params = new HashMap<>();
        params.put("reactionType", "haha");

        when(reactRepo.getReactionByUserAndPost(1, 2)).thenReturn(null);

        Post post = new Post();
        User user = new User();
        when(postRepo.getPostById(2)).thenReturn(post);
        when(userRepo.getUserById(1)).thenReturn(user);

        Reaction created = new Reaction();
        when(reactRepo.addReaction(any(Reaction.class))).thenReturn(created);

        Reaction result = reactionService.updateReaction(params, 1, 2);

        assertEquals(created, result);
        verify(reactRepo).addReaction(any(Reaction.class));
        verify(reactRepo, never()).updateReaction(any());
    }
}
