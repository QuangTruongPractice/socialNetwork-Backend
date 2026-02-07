package com.tqt.services;

import com.tqt.dto.CommentDTO;
import com.tqt.pojo.Comment;
import com.tqt.pojo.Post;
import com.tqt.pojo.User;
import com.tqt.repositories.CommentRepository;
import com.tqt.repositories.PostRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.impl.CommentServiceImpl;
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

public class CommentServiceTest extends BaseServiceTest {

    @Mock
    private CommentRepository commentRepo;

    @Mock
    private PostRepository postRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private CommentServiceImpl commentService;

    // Simple pass-through methods - 1 test each
    @Test
    public void testGetCommentByPostId_Success() {
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());
        when(commentRepo.getCommentByPostId(1)).thenReturn(comments);

        List<Comment> result = commentService.getCommentByPostId(1);

        assertEquals(2, result.size());
        verify(commentRepo).getCommentByPostId(1);
    }

    @Test
    public void testGetCommentById_Success() {
        Comment comment = new Comment();
        when(commentRepo.getCommentById(1)).thenReturn(comment);

        Comment result = commentService.getCommentById(1);

        assertEquals(comment, result);
        verify(commentRepo).getCommentById(1);
    }

    // addComment - no conditional logic, just 1 success test
    @Test
    public void testAddComment_Success() {
        Map<String, String> params = new HashMap<>();
        params.put("content", "Great post!");

        Post post = new Post();
        User user = new User();
        when(postRepo.getPostById(1)).thenReturn(post);
        when(userRepo.getUserById(2)).thenReturn(user);

        Comment saved = new Comment();
        when(commentRepo.addComment(any(Comment.class))).thenReturn(saved);

        Comment result = commentService.addComment(params, 2, 1);

        assertEquals(saved, result);
        verify(postRepo).getPostById(1);
        verify(userRepo).getUserById(2);
        verify(commentRepo).addComment(any(Comment.class));
    }

    // Complex method: updateComment - Test conditional branches
    @Test
    public void testUpdateComment_OwnerUpdatesContent_Success() {
        Map<String, String> params = new HashMap<>();
        params.put("content", "Updated content");

        User owner = new User();
        owner.setId(2);

        Comment existing = new Comment();
        existing.setUser(owner);
        existing.setContent("Old content");
        existing.setCreatedDate(java.time.LocalDate.now());

        when(commentRepo.getCommentById(1)).thenReturn(existing);
        when(commentRepo.updateComment(any(Comment.class))).thenReturn(existing);

        CommentDTO result = commentService.updateComment(params, 2, 10, 1);

        assertNotNull(result);
        assertEquals("Updated content", existing.getContent());
        assertNotNull(existing.getUpdatedDate());
        verify(commentRepo).updateComment(existing);
    }

    @Test
    public void testUpdateComment_NoContentProvided_ShouldStillUpdate() {
        Map<String, String> params = new HashMap<>();

        User owner = new User();
        owner.setId(2);

        Comment existing = new Comment();
        existing.setUser(owner);
        existing.setContent("Original");
        existing.setCreatedDate(java.time.LocalDate.now());

        when(commentRepo.getCommentById(1)).thenReturn(existing);
        when(commentRepo.updateComment(any(Comment.class))).thenReturn(existing);

        CommentDTO result = commentService.updateComment(params, 2, 10, 1);

        assertNotNull(result);
        assertEquals("Original", existing.getContent());
        verify(commentRepo).updateComment(existing);
    }

    @Test
    public void testUpdateComment_NonOwner_ShouldThrowSecurityException() {
        Map<String, String> params = new HashMap<>();
        params.put("content", "Hacked!");

        User owner = new User();
        owner.setId(2);

        Comment existing = new Comment();
        existing.setUser(owner);

        when(commentRepo.getCommentById(1)).thenReturn(existing);

        assertThrows(SecurityException.class,
                () -> commentService.updateComment(params, 999, 10, 1));

        verify(commentRepo, never()).updateComment(any());
    }

    // Complex method: deleteComment - Test all security branches
    @Test
    public void testDeleteComment_CommentOwner_Success() {
        User commentOwner = new User();
        commentOwner.setId(2);

        User postOwner = new User();
        postOwner.setId(3);

        Post post = new Post();
        post.setUser(postOwner);

        Comment comment = new Comment();
        comment.setUser(commentOwner);
        comment.setPost(post);

        when(commentRepo.getCommentById(1)).thenReturn(comment);
        when(commentRepo.updateComment(any(Comment.class))).thenReturn(comment);

        commentService.deleteComment(1, 2);

        assertTrue(comment.getIsDeleted());
        assertNotNull(comment.getUpdatedDate());
        verify(commentRepo).updateComment(comment);
    }

    @Test
    public void testDeleteComment_PostOwner_Success() {
        User commentOwner = new User();
        commentOwner.setId(2);

        User postOwner = new User();
        postOwner.setId(3);

        Post post = new Post();
        post.setUser(postOwner);

        Comment comment = new Comment();
        comment.setUser(commentOwner);
        comment.setPost(post);

        when(commentRepo.getCommentById(1)).thenReturn(comment);
        when(commentRepo.updateComment(any(Comment.class))).thenReturn(comment);

        commentService.deleteComment(1, 3);

        assertTrue(comment.getIsDeleted());
        verify(commentRepo).updateComment(comment);
    }

    @Test
    public void testDeleteComment_NeitherOwner_ShouldThrowSecurityException() {
        User commentOwner = new User();
        commentOwner.setId(2);

        User postOwner = new User();
        postOwner.setId(3);

        Post post = new Post();
        post.setUser(postOwner);

        Comment comment = new Comment();
        comment.setUser(commentOwner);
        comment.setPost(post);

        when(commentRepo.getCommentById(1)).thenReturn(comment);

        assertThrows(SecurityException.class,
                () -> commentService.deleteComment(1, 999));

        verify(commentRepo, never()).updateComment(any());
    }
}
